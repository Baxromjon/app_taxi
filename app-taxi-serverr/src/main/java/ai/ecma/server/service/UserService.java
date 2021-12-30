package ai.ecma.server.service;

import ai.ecma.server.entity.User;
import ai.ecma.server.exceptions.ResourceNotFoundException;
import ai.ecma.server.payload.Result;
import ai.ecma.server.payload.UserDto;
import ai.ecma.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * BY BAXROMJON on 04.11.2020
 */

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Result editDriver(UserDto userDto) {
        User driver = userRepository.findById(userDto.getId()).orElseThrow(() -> new ResourceNotFoundException("user", "id", userDto.getId()));
        driver.setFirstName(userDto.getFirstName());
        driver.setLastName(userDto.getLastName());
        driver.setMiddleName(userDto.getMiddleName());
        driver.setPhoneNumber(userDto.getPhoneNumber());
        driver.setBirthDate(userDto.getBirthDate());
        try {
            userRepository.save(driver);
            return new Result("Edited", true);
        } catch (Exception e) {
            return new Result("Error in saving", false);
        }
    }

    public Result changeLockedDriver(UUID driverId) {
        User driver = userRepository.findById(driverId).orElseThrow(() -> new ResourceNotFoundException("user", "id", driverId));
        boolean accountNonLocked = driver.isAccountNonLocked();
        driver.setAccountNonLocked(!accountNonLocked);
        try {
            userRepository.save(driver);
            return new Result("Change locked type", true);
        } catch (Exception e) {
            return new Result("Error in saving", false);
        }
    }
}
