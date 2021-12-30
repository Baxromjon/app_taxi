package ai.ecma.server.entity;

import ai.ecma.server.entity.enums.OrderStatus;
import ai.ecma.server.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * BY BAXROMJON on 10.11.2020
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "orders")
public class Order extends AbsEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Car car;

    private Timestamp acceptedAt;

    private Timestamp arrivedAt;

    private Timestamp startedAt;

    private Timestamp closedAt;

    @Column(nullable = false)
    private Double fare;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tariff tariff;

    @OneToMany(mappedBy = "order")
    private List<Route> routes;
}
