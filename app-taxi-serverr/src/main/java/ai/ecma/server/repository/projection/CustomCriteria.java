package ai.ecma.server.repository.projection;


import ai.ecma.server.entity.Criteria;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "customCriteria", types = Criteria.class)
public interface CustomCriteria {

    Integer getId();

    String getName();

    String getDescription();

    boolean isNegative();

    boolean isForClient();
}
