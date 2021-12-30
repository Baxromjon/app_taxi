package ai.ecma.server.service;

import ai.ecma.server.entity.Role;
import ai.ecma.server.entity.User;
import ai.ecma.server.entity.enums.RoleName;
import ai.ecma.server.payload.Result;
import ai.ecma.server.payload.UserDto;
import ai.ecma.server.repository.RoleRepository;
import ai.ecma.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * BY BAXROMJON on 30.10.2020
 */

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    public Result registerOrLogin(UserDto userDto) {
        Role role = roleRepository.findByRoleName(RoleName.ROLE_CLIENT);
        Optional<User> optionalUser = userRepository.findByPhoneNumberAndRolesIn(userDto.getPhoneNumber(), Collections.singleton(role));
        User user = optionalUser.orElseGet(User::new);
        String randomNumberString = getRandomNumberString();//sms generate
        if (!optionalUser.isPresent()) {
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setRoles(Collections.singleton(role));
            user.setPassword(passwordEncoder.encode(userDto.getPhoneNumber()));
        }
        user.setSmsCode(randomNumberString);
        userRepository.save(user);
        return new Result(randomNumberString, user.getId());
    }

    public Result loginDriver(UserDto userDto) {
        Role role = roleRepository.findByRoleName(RoleName.ROLE_DRIVER);
        Optional<User> optionalUser = userRepository.findByPhoneNumberAndRolesIn(userDto.getPhoneNumber(), Collections.singleton(role));
        if (optionalUser.isPresent()) {
            String randomNumberString = getRandomNumberString();//sms generate
            User user = optionalUser.get();
            user.setSmsCode(randomNumberString);
            userRepository.save(user);
            return new Result(randomNumberString, true, user.getId());
        }
        return new Result("Partnerga murojaat qiling", false);
    }




    public Result checkSmsCode(Result result) {
        Optional<User> optionalUser = userRepository.findById(result.getUserId());
        if (!optionalUser.isPresent()) {
            return new Result("O'chirilgan", false);
        }
        User user = optionalUser.get();
        if (user.getSmsCode().equals(result.getMessage())) {
            user.setEnabled(true);
            userRepository.save(user);
            return new Result("Ok", true);
        }
        return new Result("Kod xato", false);
    }


    private String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }


    public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        return userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new UsernameNotFoundException("userId"));
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {

        return userRepository.findFirstByPhoneNumberAndRolesNotIn(phoneNumber).orElseThrow(() -> new UsernameNotFoundException("userId"));
    }

}
