package ai.ecma.server.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BY BAXROMJON on 10.11.2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TariffDto {
    private Integer id;
    private String name;
    private Double price;
//    private Double initialPrice;

}
