package medical.app.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO - ShortElectDto необходима для передачи данных клиенту и общения с ним
 */
@Getter
@Setter
public class ShortElectDto {
    private LocalDateTime appointmentDateTime;
    private String data;
}
