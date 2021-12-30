package ai.ecma.server.repository;

import ai.ecma.server.entity.MissedOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MissedOrderRepository extends JpaRepository<MissedOrder, UUID> {
    boolean existsByCarIdAndOrderId(UUID car_id, UUID order_id);

}
