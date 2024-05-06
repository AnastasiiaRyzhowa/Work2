package medical.app.entity.dto;

import lombok.Getter;
import lombok.Setter;
import medical.app.entity.Node;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO - ElectrocardiographyDataDto необходима для передачи данных клиенту и общения с ним
 */
@Setter
@Getter
public class ElectrocardiographyDataDto {
    private UUID id;
    private LocalDateTime appointmentDateTime;
    private String data;

    private List<NodeDto> nodes;
}
