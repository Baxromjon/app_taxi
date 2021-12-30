package ai.ecma.server.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * BY BAXROMJON on 16.11.2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderReportSecondDto {
    private UUID id;
    private String phoneNumber;
    private String firstName;
    private String driverPhoneNumber;
    private Double fare;
    private Double distance;
    private Timestamp createdAt;
    private Timestamp closedAt;
}
