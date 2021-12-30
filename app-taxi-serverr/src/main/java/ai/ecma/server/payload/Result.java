package ai.ecma.server.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result {
    private String message;
    private boolean success;
    private UUID userId;

    public Result(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public Result(String message, UUID userId) {
        this.message = message;
        this.userId = userId;
    }
}
