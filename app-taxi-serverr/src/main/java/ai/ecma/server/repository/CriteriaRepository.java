package ai.ecma.server.repository;

import ai.ecma.server.entity.Criteria;
import ai.ecma.server.repository.projection.CustomCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "criteria",collectionResourceRel = "list",excerptProjection = CustomCriteria.class)
public interface CriteriaRepository extends JpaRepository<Criteria, Integer> {

}
