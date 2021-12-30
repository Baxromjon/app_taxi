package ai.ecma.server.repository;

import ai.ecma.server.entity.BotUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BotUserRepository extends JpaRepository<BotUser, UUID> {
    Optional<BotUser> findByChatId(Long chatId);

    List<BotUser> findAllByPhoneNumberIn(Collection<String> phoneNumber);
}
