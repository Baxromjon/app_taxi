package ai.ecma.server.utils;

import ai.ecma.server.exceptions.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * BY BAXROMJON on 09.11.2020
 */

public class CommonUtils {


    public static Pageable getPageable(int page, int size) {
        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Sahifadagi ma'lumotlar soni ko'p");
        }
        if (page < 0) {
            throw new BadRequestException("Sahifadagi soni 0 dan kichik bo'lishi mumkin emas");
        }
        return PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
    }

    public static Pageable getPageable(int page, int size, boolean asc, String... properties) {
        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Sahifadagi ma'lumotlar soni ko'p");
        }
        if (page < 0) {
            throw new BadRequestException("Sahifadagi soni 0 dan kichik bo'lishi mumkin emas");
        }
        return PageRequest.of(page, size, asc ? Sort.Direction.ASC : Sort.Direction.DESC, properties);
    }
}
