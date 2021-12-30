package ai.ecma.server.repository;

import ai.ecma.server.entity.Order;
import ai.ecma.server.entity.enums.OrderStatus;
import ai.ecma.server.payload.OrderReportDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    boolean existsByIdAndStatus(UUID id, OrderStatus status);

    Optional<Order> findByIdAndStatus(UUID id, OrderStatus status);


    @Query(value = "select cast(driver_id as varchar) from car\n" +
            "join orders o on car.id = o.car_id\n" +
            "where o.id=:orderId", nativeQuery = true)
    UUID getDriverIdByOrderId(@Param("orderId") UUID orderId);

    @Query(value = "select cast(o.id as varchar),\n" +
            "       u.phone_number phoneNumber,\n" +
            "       d.first_name firstName,\n" +
            "       d.phone_number driverPhoneNumber,\n" +
            "       o.fare,\n" +
            "       coalesce((select calculate_distance_all_route_by_order(o.id)),10) as distance,\n" +
            "       o.created_at createdAt,\n" +
            "       o.closed_at closedAt\n" +
            "from orders o\n" +
            "         join users u on o.created_by = u.id\n" +
            "         join car c on o.car_id = c.id\n" +
            "         join users d on d.id = c.driver_id\n" +
            "limit :size offset :page", nativeQuery = true)
    List<OrderReportDto> getOrdersForReport(@Param("page") int page, @Param("size") int size);

    @Query(value = "select cast(o.id as varchar),\n" +
            "       u.phone_number phoneNumber,\n" +
            "       d.first_name firstName,\n" +
            "       d.phone_number driverPhoneNumber,\n" +
            "       o.fare,\n" +
            "       coalesce((select calculate_distance_all_route_by_order(o.id)),10) as distance,\n" +
            "       o.created_at createdAt,\n" +
            "       o.closed_at closedAt\n" +
            "from orders o\n" +
            "         join users u on o.created_by = u.id\n" +
            "         join car c on o.car_id = c.id\n" +
            "         join users d on d.id = c.driver_id\n" +
            "limit :size offset :page", nativeQuery = true)
    List<Object[]> getOrdersForReportSecond(@Param("page") int page, @Param("size") int size);
}
