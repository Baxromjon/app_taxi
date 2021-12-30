package ai.ecma.server.controller;

import ai.ecma.server.entity.Route;
import ai.ecma.server.exceptions.ResourceNotFoundException;
import ai.ecma.server.payload.OrderReportDto;
import ai.ecma.server.payload.OrderReportSecondDto;
import ai.ecma.server.payload.Result;
import ai.ecma.server.payload.TariffDto;
import ai.ecma.server.repository.OrderRepository;
import ai.ecma.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * BY BAXROMJON on 10.11.2020
 */

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @PostMapping("/calculatePriceFromLocationList")
    public HttpEntity<?> calculatePriceFromLocationList(@RequestBody List<Route> routes) {
        List<TariffDto> tariffs = orderService.calculatePriceFromLocationList(routes);
        return ResponseEntity.ok(tariffs);
    }

    @PostMapping("/selectTariffAndCreateOrder/{tariffId}")
    public HttpEntity<?> selectTariffAndCreateOrder(@PathVariable Integer tariffId, @RequestBody List<Route> routes) {
        Result result = orderService.selectTariffAndCreateOrder(tariffId, routes);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAvailableCar/{orderId}")
    public HttpEntity<?> getAvailableCar(@PathVariable UUID orderId) throws TelegramApiException, InterruptedException {
        Result result = orderService.getAvailableCar(orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("", "", orderId)));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getOrdersForReport")
    public HttpEntity<?> getOrdersForReport() {
        List<OrderReportDto> ordersForReport = orderRepository.getOrdersForReport(0, 10);
        return ResponseEntity.ok(ordersForReport);
    }

    public OrderReportSecondDto getSecondDto(Object[] objects) {
        return new OrderReportSecondDto(
                UUID.fromString(objects[0].toString()),
                objects[1].toString(),
                objects[2].toString(),
                objects[3].toString(),
                Double.parseDouble(objects[4].toString()),
                Double.parseDouble(objects[5].toString()),
                Timestamp.valueOf(objects[6].toString()),
                Timestamp.valueOf(objects[7].toString())
        );
    }


}
