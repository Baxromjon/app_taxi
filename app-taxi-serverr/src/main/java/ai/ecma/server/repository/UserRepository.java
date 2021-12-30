package ai.ecma.server.repository;

import ai.ecma.server.entity.Role;
import ai.ecma.server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * BY BAXROMJON on 30.10.2020
 */


public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByPhoneNumberAndRolesIn(String phoneNumber, Set<Role> roles);

    @Query(value = "select *\n" +
            "from users\n" +
            "where phone_number = :phoneNumber\n" +
            "  and id in (\n" +
            "    select user_id\n" +
            "    from user_role\n" +
            "    where user_id = id\n" +
            "      and role_id\n" +
            "        in (select id from role where role_name <> 'ROLE_CLIENT' AND role_name <> 'ROLE_DRIVER')\n" +
            ")", nativeQuery = true)
    Optional<User> findFirstByPhoneNumberAndRolesNotIn(@Param("phoneNumber") String phoneNumber);

    @Query(value = "select *\n" +
            "from users\n" +
            "where phone_number = :phoneNumber\n" +
            "  and id in (\n" +
            "    select user_id\n" +
            "    from user_role\n" +
            "    where user_id = id\n" +
            "      and role_id\n" +
            "        in (select id from role where role_name=:roleName)\n" +
            ")", nativeQuery = true)
    Optional<User> findByPhoneNumberAndRoleName(@Param("phoneNumber") String phoneNumber, @Param("roleName") String roleName);


}
