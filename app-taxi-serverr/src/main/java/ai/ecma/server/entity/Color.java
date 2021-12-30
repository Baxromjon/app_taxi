package ai.ecma.server.entity;

import ai.ecma.server.entity.template.AbsNameEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Color extends AbsNameEntity {

    @Column(nullable = false, unique = true)
    private String code;

    @Column(columnDefinition = "text")
    private String description;
}
