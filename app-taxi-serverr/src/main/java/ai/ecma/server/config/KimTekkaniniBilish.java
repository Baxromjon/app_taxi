package ai.ecma.server.config;

import ai.ecma.server.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

/**
 * BY BAXROMJON on 10.11.2020
 */
@Configuration
@EnableJpaAuditing
public class KimTekkaniniBilish {
    @Bean
    AuditorAware<UUID> yozKimligini() {
        return new SistemadaKimYurganiniQaytaradigan();
    }
}

class SistemadaKimYurganiniQaytaradigan implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())))
            return Optional.ofNullable(((User) authentication.getPrincipal()).getId());
        return Optional.empty();
    }
}
