package ai.ecma.server.controller;

import ai.ecma.server.entity.User;
import ai.ecma.server.payload.CarDto;
import ai.ecma.server.payload.Result;
import ai.ecma.server.security.CurrentUser;
import ai.ecma.server.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

/**
 * BY BAXROMJON on 03.11.2020
 */

@RestController
@RequestMapping("/api/car")
public class CarController {
    @Autowired
    CarService carService;

    @PreAuthorize("hasAnyRole('ROLE_PARTNER')")
    @PostMapping
    public HttpEntity<?> addCar(@Valid @RequestBody CarDto carDto) {
        Result result = carService.addCar(carDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    @PreAuthorize("hasAnyRole('ROLE_PARTNER')")
    @PutMapping("/{id}")
    public HttpEntity<?> editCar(@PathVariable UUID id, @RequestBody CarDto carDto) {
        Result result = carService.editCar(id, carDto);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    @PutMapping("/changeOnline")
    public HttpEntity<?> changeOnline(@CurrentUser User user) {
        Result result = carService.changeOnline(user);
        return ResponseEntity.ok(result);
    }


    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    @PutMapping("/addTariffList/{tariffId}")
    public HttpEntity<?> addTariffList(@PathVariable Integer tariffId, @CurrentUser User driver) {
        Result result = carService.addTariffList(tariffId, driver);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(result);
    }

    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    @PutMapping("/removeTariffList/{tariffId}")
    public HttpEntity<?> removeTariffList(@PathVariable Integer tariffId, @CurrentUser User driver) {
        Result result = carService.removeTariffList(tariffId, driver);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(result);
    }

    @PreAuthorize("hasAnyRole('ROLE_DRIVER')")
    @PutMapping("/updateLocation/{lat}/{lon}")
    public HttpEntity<?> updateLocation(@CurrentUser User user, @PathVariable Float lat, @PathVariable Float lon) {
        Result result = carService.updateLocation(user, lat, lon);
        return ResponseEntity.ok(result);
    }
}
