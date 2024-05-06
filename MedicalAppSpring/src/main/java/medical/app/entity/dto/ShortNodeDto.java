package medical.app.entity.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO - ShortNodeDto необходима для передачи данных клиенту и общения с ним
 */
@Getter
@Setter
public class ShortNodeDto {
    private String label;
    private Double x;
    private Double y;
}
