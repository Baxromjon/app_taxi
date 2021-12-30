package ai.ecma.server.repository;

import ai.ecma.server.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RateRepository extends JpaRepository<Rate, UUID> {

    @Query(value = "select r.* from rate r\n" +
            "join orders o on r.order_id = o.id\n" +
            "join car c on o.car_id = c.id\n" +
            "join users u on c.driver_id = u.id\n" +
            "where c.driver_id=r.created_by and o.id=:orderId",nativeQuery = true)
    Optional<Rate> findByOrderIdAndDriverRate(@Param("orderId") UUID orderId);


}
