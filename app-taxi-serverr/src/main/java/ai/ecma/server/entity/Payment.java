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
 * BY BAXROMJON on 04.11.2020
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payment extends AbsEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Car car;

    @Column(nullable = false)
    private Double paySum;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private PayType payType;
}
