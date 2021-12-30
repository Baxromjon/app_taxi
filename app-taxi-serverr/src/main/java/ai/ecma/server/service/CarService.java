package ai.ecma.server.service;

import ai.ecma.server.entity.*;
import ai.ecma.server.entity.enums.RoleName;
import ai.ecma.server.exceptions.ResourceNotFoundException;
import ai.ecma.server.payload.CarDto;
import ai.ecma.server.payload.Result;
import ai.ecma.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

/**
 * BY BAXROMJON on 03.11.2020
 */

@Service
public class CarService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    CarRepository carRepository;
    @Autowired
    ColorRepository colorRepository;
    @Autowired
    ModelRepository modelRepository;
    @Autowired
    MadeYearRepository madeYearRepository;
    @Autowired
    TariffRepository tariffRepository;

    /**
     * BU YERDA MASHINA QO'SHILYAPTI. SHU BILAN BIRGA HAYDOVCHI HAM QO'SHILYAPTI(USER)
     *
     * @param carDto
     * @return
     */

    @Transactional(noRollbackFor = {NullPointerException.class})
    public Result addCar(CarDto carDto) {
        User driver = new User();
        driver.setFirstName(carDto.getUserDto().getFirstName());
        driver.setLastName(carDto.getUserDto().getLastName());
        driver.setMiddleName(carDto.getUserDto().getMiddleName());
        driver.setPhoneNumber(carDto.getUserDto().getPhoneNumber());
        driver.setBirthDate(carDto.getUserDto().getBirthDate());
        driver.setPassword(passwordEncoder.encode(carDto.getUserDto().getPhoneNumber()));
        Role role = roleRepository.findByRoleName(RoleName.ROLE_DRIVER);
        driver.setRoles(Collections.singleton(role));

        userRepository.save(driver);

        Car car = new Car();

//        Optional<Color> color = colorRepository.findById(carDto.getColorId());
        car.setModel(modelRepository.findById(carDto.getModelId()).orElseThrow(() -> new ResourceNotFoundException("model", "id", carDto.getModelId())));
        Color color = colorRepository.findById(carDto.getColorId()).orElseThrow(() -> new ResourceNotFoundException("Color", "id", carDto.getColorId()));
        car.setColor(color);
        car.setCarLicenseSerial(carDto.getCarLicenseSerial());
        car.setCarLicenseSerialNumber(carDto.getCarLicenseSerialNumber());
        car.setDriverLicenseSerial(carDto.getDriverLicenseSerial());
        car.setDriverLicenseSerialNumber(carDto.getDriverLicenseSerialNumber());
        car.setRegStateNumber(carDto.getRegStateNumber());
        car.setDriver(driver);
        car.setMadeYear(madeYearRepository.findById(carDto.getMadeYearId()).orElseThrow(() -> new ResourceNotFoundException("Year", "Id", carDto.getMadeYearId())));
        carRepository.save(car);
        return new Result("Saved car", true);
    }


    /**
     * BU YERDA CAR EDIT QILINYAPTI
     *
     * @param id
     * @param carDto
     * @return
     */
    public Result editCar(UUID id, CarDto carDto) {
        Car car = carRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("car", "id", id));
        car.setModel(modelRepository.findById(carDto.getModelId()).orElseThrow(() -> new ResourceNotFoundException("model", "id", carDto.getModelId())));
        car.setColor(colorRepository.findById(carDto.getColorId()).orElseThrow(() -> new ResourceNotFoundException("color", "id", carDto.getColorId())));
        car.setMadeYear(madeYearRepository.findById(carDto.getMadeYearId()).orElseThrow(() -> new ResourceNotFoundException("madeYear", "id", carDto.getMadeYearId())));
        car.setCarLicenseSerial(carDto.getCarLicenseSerial());
        car.setCarLicenseSerialNumber(carDto.getCarLicenseSerialNumber());
        car.setDriverLicenseSerial(carDto.getDriverLicenseSerial());
        car.setDriverLicenseSerialNumber(carDto.getDriverLicenseSerialNumber());
        car.setRegStateNumber(carDto.getRegStateNumber());
        try {
            carRepository.save(car);
            return new Result("Edited", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result("Error in saving", false);
        }
    }


    /**
     * MASHINANI ONLINE STATUSGA O'TKAZISH UCHUN
     *
     * @param driver
     * @return
     */
    public Result changeOnline(User driver) {
        Car car = carRepository.findByDriverId(driver.getId()).orElseThrow(() -> new ResourceNotFoundException("car", "driverId", driver.getId()));
        boolean online = car.isOnline();
        car.setOnline(!online);
        carRepository.save(car);
        return new Result("Success changed to online", true);
    }

    public Result addTariffList(Integer tariffId, User driver) {
        Car car = carRepository.findByDriverId(driver.getId()).orElseThrow(() -> new ResourceNotFoundException("car", "driverId", driver.getId()));
        Tariff tariff = tariffRepository.findById(tariffId).orElseThrow(() -> new ResourceNotFoundException("tariff", "id", tariffId));
        Model model = car.getModel();
        Set<Model> models = tariff.getModels();
        if (models.contains(model)) {
            car.getTariffs().add(tariff);
            carRepository.save(car);
            return new Result("Changed", true);
        }
        return new Result("Mumkin emas", false);
    }

    public Result removeTariffList(Integer tariffId, User driver) {
        Car car = carRepository.findByDriverId(driver.getId()).orElseThrow(() -> new ResourceNotFoundException("car", "driverId", driver.getId()));
        Tariff tariff = tariffRepository.findById(tariffId).orElseThrow(() -> new ResourceNotFoundException("tariff", "id", tariffId));
        car.getTariffs().remove(tariff);
        carRepository.save(car);
        return new Result("Changed", true);
    }

    public Result updateLocation(User driver, Float lat, Float lon) {
        Car car = carRepository.findByDriverId(driver.getId()).orElseThrow(() -> new ResourceNotFoundException("car", "driverId", driver.getId()));
        car.setLat(lat);
        car.setLon(lon);
        carRepository.save(car);
        return new Result("Edited", true);
    }
}
