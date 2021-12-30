package ai.ecma.server.entity;

import ai.ecma.server.entity.template.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 * BY BAXROMJON on 10.11.2020
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Route extends AbsEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @Column(nullable = false)
    private Float fromLat;

    @Column(nullable = false)
    private Float fromLon;

    private Float toLat;

    private Float toLon;

    private Integer routeIndex;
}
