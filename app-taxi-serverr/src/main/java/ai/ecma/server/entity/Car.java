package ai.ecma.server.entity;

import ai.ecma.server.entity.enums.OrderStatus;
import ai.ecma.server.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

/**
 * BY BAXROMJON on 03.11.2020
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"carLicenseSerial", "carLicenseSerialNumber"}),
        @UniqueConstraint(columnNames = {"driverLicenseSerial", "driverLicenseSerialNumber"})
})
public class Car extends AbsEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Model model;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Color color;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private MadeYear madeYear;

    @Column(nullable = false)
    private String carLicenseSerial;

    @Column(nullable = false)
    private String carLicenseSerialNumber;

    @Column(nullable = false)
    private String driverLicenseSerial;

    @Column(nullable = false)
    private String driverLicenseSerialNumber;

    @Column(nullable = false, unique = true)
    private String regStateNumber;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private User driver;

    private boolean online;

    @ManyToMany
    @JoinTable(name = "car_tariff",
            joinColumns = {@JoinColumn(name = "car_id")},
            inverseJoinColumns = {@JoinColumn(name = "tariff_id")})
    private Set<Tariff> tariffs;

    private Float lat;

    private Float lon;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
