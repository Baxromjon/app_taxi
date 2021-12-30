package ai.ecma.server.repository.projection;

import ai.ecma.server.entity.Brand;
import ai.ecma.server.entity.Model;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "customModel", types = Model.class)
public interface CustomModel {
    Integer getId();

    String getName();

    String getDescription();

    @Value("#{target.brand.id}")
    Integer getBrandId();

    @Value("#{target.brand.name}")
    String getBrandName();
}
