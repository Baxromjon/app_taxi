package ai.ecma.server.repository;

import ai.ecma.server.entity.Role;
import ai.ecma.server.entity.User;
import ai.ecma.server.entity.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

/**
 * BY BAXROMJON on 30.10.2020
 */

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(RoleName roleName);
}
