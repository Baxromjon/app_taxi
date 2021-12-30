package ai.ecma.server.controller;

import ai.ecma.server.entity.User;
import ai.ecma.server.payload.Result;
import ai.ecma.server.payload.UserDto;
import ai.ecma.server.security.JwtTokenProvider;
import ai.ecma.server.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * BY BAXROMJON on 29.10.2020
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/forClient")
    public HttpEntity<?> registerOrLoginCustomer(@RequestBody UserDto userDto) {
        Result result = authService.registerOrLogin(userDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/forDriver")
    public HttpEntity<?> loginDriver(@RequestBody UserDto userDto) {
        Result result = authService.loginDriver(userDto);
        return ResponseEntity.status(result.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(result);
    }

    @PostMapping("/checkSmsCode")
    public HttpEntity<?> checkSmsCode(@RequestBody Result result) {
        Result javob = authService.checkSmsCode(result);
        if (javob.isSuccess()) {
            return ResponseEntity.status(200).body(jwtTokenProvider.generateToken(result.getUserId()));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(javob);
    }

    @PostMapping("/login")
    public HttpEntity<?> checkLogin(@RequestBody UserDto userDto) {
        try {
            Authentication paroliVaLoginiTogriOdam =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            userDto.getPhoneNumber(),
                            userDto.getPassword()
                    ));
            User user = (User) paroliVaLoginiTogriOdam.getPrincipal();
            String token = jwtTokenProvider.generateToken(user.getId());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("Bad credential");
    }
}
