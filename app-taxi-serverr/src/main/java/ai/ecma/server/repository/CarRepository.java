package ai.ecma.server.repository;

import ai.ecma.server.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {

    Optional<Car> findByDriverId(UUID driver_id);

    @Query(value = "select *\n" +
            "from car c\n" +
            "join users u on c.driver_id=u.id\n" +
            "where u.phone_number=:phoneNumber", nativeQuery = true)
    Optional<Car> findByDriverPhoneNumber(@Param("phoneNumber") String phoneNumber);


    @Query(value = "with near_cars as (select c.*, (select calculate_distance(:lat, :lon, c.lat, c.lon)) dis\n" +
            "                   from car c\n" +
            "                            join car_tariff ct on c.id = ct.car_id\n" +
            "                   where c.online = true\n" +
            "                     and (select calculate_distance(:lat, :lon, c.lat, c.lon)) <= 10000\n" +
            "                     and ct.tariff_id = :tariffId\n" +
            "                     and (((select sum(pay_sum) from payment where car_id = c.id) -\n" +
            "                           (select coalesce(sum(e.amount), 0)\n" +
            "                            from expense e\n" +
            "                                     join orders o on e.order_id = o.id\n" +
            "                            where o.car_id = c.id)) >= :orderSum)\n" +
            "                     and (((select count(*) from orders o where o.status = 'WAITING' or o.status = 'IN_PROGRESS') = 0)\n" +
            "                       or (with last_route as (select *\n" +
            "                                               from route r\n" +
            "                                                        join\n" +
            "                                                    (select *\n" +
            "                                                     from orders o\n" +
            "                                                     where o.car_id = c.id\n" +
            "                                                     order by o.created_at desc\n" +
            "                                                     limit 1) orr on r.order_id = orr.id\n" +
            "                                               order by r.route_index desc\n" +
            "                                               limit 1)\n" +
            "                               (select calculate_distance(\n" +
            "                                               :lat,\n" +
            "                                               :lon,\n" +
            "                                               (select to_lat from last_route),\n" +
            "                                               (select to_lon from last_route)) < 500))))\n" +
            "select *\n" +
            "from near_cars\n" +
            "order by near_cars.dis\n" +
            "limit :size offset :page*:size", nativeQuery = true)
    List<Car> findAllByNearAndOtherFields(@Param("lat") Float lat,
                                          @Param("lon") Float lon,
                                          @Param("tariffId") Integer tariffId,
                                          @Param("orderSum") Double orderSum,
                                          @Param("size") int size,
                                          @Param("page") int page);
}
