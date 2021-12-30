package ai.ecma.server.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * BY BAXROMJON on 05.11.2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDto {
    private UUID id;

    private String payerFullName;

    private String carRegStateNumber;

    private String payTypeName;

    private Double paySum;

    private Timestamp payDate;

    private String phoneNumber;

    public PaymentDto(UUID id, String payerFullName, String carRegStateNumber, String payTypeName, Double paySum, Timestamp payDate) {
        this.id = id;
        this.payerFullName = payerFullName;
        this.carRegStateNumber = carRegStateNumber;
        this.payTypeName = payTypeName;
        this.paySum = paySum;
        this.payDate = payDate;
    }
}
