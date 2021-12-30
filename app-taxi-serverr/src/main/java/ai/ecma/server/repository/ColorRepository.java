package ai.ecma.server.repository;

import ai.ecma.server.entity.Color;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ColorRepository extends JpaRepository<Color,Integer> {
    boolean existsByName(String name);
    boolean existsByCode(String code);
//    boolean existsById(Integer id);


}
