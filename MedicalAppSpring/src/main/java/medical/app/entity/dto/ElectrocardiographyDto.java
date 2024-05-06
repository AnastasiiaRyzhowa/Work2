package medical.app.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO - ElectrocardiographyDto необходима для передачи данных клиенту и общения с ним
 */
@Getter
@Setter
public class ElectrocardiographyDto {
    private UUID id;
    private LocalDateTime appointmentDateTime;
}
