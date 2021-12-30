package ai.ecma.server.service;

import ai.ecma.server.bot.BotServiceForDriver;
import ai.ecma.server.bot.BotTaxiDriver;
import ai.ecma.server.entity.*;
import ai.ecma.server.entity.enums.OrderStatus;
import ai.ecma.server.exceptions.ResourceNotFoundException;
import ai.ecma.server.payload.Result;
import ai.ecma.server.payload.TariffDto;
import ai.ecma.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BY BAXROMJON on 10.11.2020
 */

@Service
public class OrderService {
    @Autowired
    TariffRepository tariffRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    RouteRepository routeRepository;
    @Autowired
    CarRepository carRepository;
    @Autowired
    BotUserRepository botUserRepository;
    @Autowired
    BotTaxiDriver botTaxiDriver;
    @Autowired
    BotServiceForDriver botServiceForDriver;
    @Autowired
    MissedOrderRepository missedOrderRepository;


    public List<TariffDto> calculatePriceFromLocationList(List<Route> routes) {
        List<Tariff> tariffList = tariffRepository.findAll();
        List<TariffDto> tariffDtoList = new ArrayList<>();
        for (Tariff tariff : tariffList) {
            TariffDto tariffDto = new TariffDto();
            tariffDto.setId(tariff.getId());
            tariffDto.setName(tariff.getName());
            tariffDto.setPrice(getFareSum(routes, tariff));
            tariffDtoList.add(tariffDto);
        }
        return tariffDtoList;
    }

    public Result selectTariffAndCreateOrder(Integer tariffId, List<Route> routes) {
        Order order = new Order();
        Tariff tariff = tariffRepository.findById(tariffId).orElseThrow(() -> new ResourceNotFoundException("tariff", "id", tariffId));
        order.setTariff(tariff);
        order.setStatus(OrderStatus.NEW);
        Double fareSum = getFareSum(routes, tariff);
        order.setFare(fareSum);
        orderRepository.save(order);
        int ketmon = 0;
        for (Route route : routes) {
            route.setOrder(order);
            route.setRouteIndex(++ketmon);
        }
        routeRepository.saveAll(routes);
        return new Result("Searching", true);
    }

    public Result getAvailableCar(Order order) throws TelegramApiException, InterruptedException {
        new Thread(()-> {
            try {
                sendToDriverInOrderService(order);
            } catch (TelegramApiException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        return new Result("Send", true);

//        return null;
    }

    private Double getFareSum(List<Route> routes, Tariff tariff) {
        double meter = meterDistanceBetweenMorePoints(routes);
        return (meter / 1000) * tariff.getPricePerKm() + tariff.getInitialPrice();
    }

    private double meterDistanceBetweenMorePoints(List<Route> routes) {
        double allMeter = 0;
        for (Route route : routes) {
            allMeter += meterDistanceBetweenTwoPoints(
                    route.getFromLat(),
                    route.getFromLon(),
                    route.getToLat(),
                    route.getToLon());
        }
        return allMeter;
    }

    private double meterDistanceBetweenTwoPoints(float fromLat, float fromLon, float toLat, float toLon) {
        float pk = (float) (180.f / Math.PI);

        float a1 = fromLat / pk;
        float a2 = fromLon / pk;
        float b1 = toLat / pk;
        float b2 = toLon / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);
        return 6366000 * tt;
    }

    private void sendToDriverInOrderService(Order order) throws TelegramApiException, InterruptedException {
        Route route = order.getRoutes().get(0);
        Float fromLat = route.getFromLat();
        Float fromLon = route.getFromLon();
        List<Car> cars = carRepository.findAllByNearAndOtherFields(fromLat,
                fromLon,
                order.getTariff().getId(),
                order.getFare(),
                10, 0);
        List<String> phoneNumbers = cars.stream().map(car -> car.getDriver().getPhoneNumber()).collect(Collectors.toList());
        List<BotUser> botUsers = botUserRepository.findAllByPhoneNumberIn(phoneNumbers);
        List<Route> routes = order.getRoutes();
        for (int i = 0; i < botUsers.size(); i++) {
            BotUser botUser = botUsers.get(i);
            botTaxiDriver.execute(botServiceForDriver.sendOrderToDriver(botUser, order, routes));
            System.out.println("uxladi");
            System.out.println(System.currentTimeMillis());
            Thread.sleep(10000);
            System.out.println("ishladi");
            System.out.println(System.currentTimeMillis());
            boolean newStill = orderRepository.existsByIdAndStatus(order.getId(), OrderStatus.NEW);
            if (newStill) {
                int finalI = i;
                new Thread(() -> saveMissedOrder(cars.get(finalI), order)).start();
            } else
                break;
        }
    }

    private void saveMissedOrder(Car car, Order order) {
        missedOrderRepository.save(new MissedOrder(
                car,
                order
        ));
    }
}
