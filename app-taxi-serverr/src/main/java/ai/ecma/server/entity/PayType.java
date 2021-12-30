package ai.ecma.server.entity;

import ai.ecma.server.entity.enums.PayTypeEnum;
import ai.ecma.server.entity.template.AbsNameEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class PayType extends AbsNameEntity {

    @Enumerated(EnumType.STRING)
    private PayTypeEnum payTypeEnum;
}
