package ai.ecma.server.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BY BAXROMJON on 06.11.2020
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageableDto {
    private Object object;
    private long totalElements;
    private int totalPages;
    private int size;
    private int currentPage;
}
