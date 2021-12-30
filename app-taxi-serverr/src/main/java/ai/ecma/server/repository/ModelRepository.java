package ai.ecma.server.repository;

import ai.ecma.server.entity.Model;
import ai.ecma.server.repository.projection.CustomModel;
import ai.ecma.server.repository.projection.CustomModelOwn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "model", collectionResourceRel = "list",excerptProjection = CustomModel.class)
public interface ModelRepository extends JpaRepository<Model, Integer> {
    boolean existsByName(String name);

    @RestResource(path = "/test")
    @Query(value = "select m.id as id,m.name as name,b.id as brandId,b.name as brandName from model m join brand b on m.brand_id = b.id", nativeQuery = true)
    List<CustomModelOwn> testModelForCustom();

}
