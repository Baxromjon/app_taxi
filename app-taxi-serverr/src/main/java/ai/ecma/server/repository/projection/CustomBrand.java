package ai.ecma.server.repository.projection;

import ai.ecma.server.entity.Brand;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "customBrand", types = Brand.class)
public interface CustomBrand {

    Integer getId();

    String getName();

    String getDescription();
}
