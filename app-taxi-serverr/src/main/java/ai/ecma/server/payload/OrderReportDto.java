package ai.ecma.server.payload;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * BY BAXROMJON on 16.11.2020
 */


public interface OrderReportDto {
    UUID getId();

    String getPhoneNumber();

    String getFirstName();

    String getDriverPhoneNumber();

    Double getFare();

    Double getDistance();

    Timestamp getCreatedAt();

    Timestamp getClosedAt();

}
