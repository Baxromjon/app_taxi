package ai.ecma.server.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * BY BAXROMJON on 11.11.2020
 */
//
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private UUID id;

    private String clientPhoneNumber;

    private String driverPhoneNumber;

    private String driverFirstName;

    private String driverLastName;

    private String carRegStateNumber;

    private String carModel;

    private String carColor;

    private Double fare;

    private Double orderDistance;
}
