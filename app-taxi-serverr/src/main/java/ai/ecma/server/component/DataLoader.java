package ai.ecma.server.component;

import ai.ecma.server.entity.User;
import ai.ecma.server.entity.enums.RoleName;
import ai.ecma.server.repository.RoleRepository;
import ai.ecma.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

/**
 * BY BAXROMJON on 03.11.2020
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Value(value = "${spring.datasource.initialization-mode}")
    private String initialMode;

    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")) {
            User superAdmin = new User(
                    "SuperAdmin",
                    "SuperAdminov",
                    "+998996791136",
                    passwordEncoder.encode("root123"),
                    Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_SUPER_ADMIN)),
                    true);

            User paynet = new User("Paynet",
                    "Paynet",
                    "paynet",
                    passwordEncoder.encode("paynet"),
                    Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_PAYNET)),
                    true
            );
            User payme = new User("Payme",
                    "Payme",
                    "payme",
                    passwordEncoder.encode("payme"),
                    Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_PAYME)),
                    true
            );
            User click = new User("Click",
                    "Click",
                    "click",
                    passwordEncoder.encode("click"),
                    Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_CLICK)),
                    true
            );
            User upay = new User("Upay",
                    "Upay",
                    "upay",
                    passwordEncoder.encode("upay"),
                    Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_UPAY)),
                    true
            );
            User oson = new User("Oson",
                    "Oson",
                    "oson",
                    passwordEncoder.encode("oson"),
                    Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_OSON)),
                    true
            );
            userRepository.saveAll(Arrays.asList(superAdmin, click, payme, oson, upay, paynet));
        }
    }
}
