package ai.ecma.server.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * BY BAXROMJON on 03.11.2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {

    @NotNull
    private Integer modelId;
    @NotNull
    private Integer colorId;
    @NotNull
    private Integer madeYearId;
    @NotBlank
    private String carLicenseSerial;
    @NotBlank
    private String carLicenseSerialNumber;
    @NotBlank
    private String driverLicenseSerial;
    @NotBlank
    private String driverLicenseSerialNumber;
    @NotBlank
    private String regStateNumber;

    @NotNull
    @Valid
    private UserDto userDto;
}
