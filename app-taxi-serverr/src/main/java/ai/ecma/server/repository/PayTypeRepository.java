package ai.ecma.server.repository;

import ai.ecma.server.entity.PayType;
import ai.ecma.server.entity.enums.PayTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "payType")
public interface PayTypeRepository extends JpaRepository<PayType, Integer> {
    boolean existsByName(String name);

    Optional<PayType> findByPayTypeEnum(PayTypeEnum payTypeEnum);
}
