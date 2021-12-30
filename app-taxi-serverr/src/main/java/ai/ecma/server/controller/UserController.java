package ai.ecma.server.controller;

import ai.ecma.server.payload.Result;
import ai.ecma.server.payload.UserDto;
import ai.ecma.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

/**
 * BY BAXROMJON on 04.11.2020
 */

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PutMapping("/editDriver")
    public HttpEntity<?> editDriver(@Valid @RequestBody UserDto userDto) {
        Result result = userService.editDriver(userDto);
        return ResponseEntity.status(result.isSuccess() ? 202 : 409).body(result);
    }

    @PutMapping("/changeLockedDriver/{driverId}")
    public HttpEntity<?> changeLockedDriver(@PathVariable UUID driverId) {
        Result result = userService.changeLockedDriver(driverId);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT).body(result);
    }
}
