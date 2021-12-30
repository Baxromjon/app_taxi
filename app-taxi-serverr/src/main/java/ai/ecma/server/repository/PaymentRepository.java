package ai.ecma.server.repository;

import ai.ecma.server.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Page<Payment> findAllByCreatedAtBetween(Timestamp begin, Timestamp end, Pageable pageable);

}
