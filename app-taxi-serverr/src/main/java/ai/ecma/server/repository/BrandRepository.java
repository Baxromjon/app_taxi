package ai.ecma.server.repository;

import ai.ecma.server.entity.Brand;
import ai.ecma.server.repository.projection.CustomBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;


@RepositoryRestResource(path = "brand", collectionResourceRel = "list", excerptProjection = CustomBrand.class)
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    boolean existsByName(String name);

    @RestResource(path = "/byName")
    List<Brand> findAllByNameContainsIgnoreCaseOrderByNameAsc(@Param("name") String name);

    @RestResource(path = "/byNameOrDescription")
    List<Brand> findAllByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(@Param("name") String name, @Param("descr") String descr);

    @RestResource(path = "/byNameAndDescription")
    List<Brand> findAllByNameContainsIgnoreCaseAndDescriptionContainsIgnoreCase(@Param("name") String name, @Param("descr") String descr);

}
