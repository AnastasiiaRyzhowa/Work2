package medical.app.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * DTO - NodeDto необходима для передачи данных клиенту и общения с ним
 */
@Getter
@Setter
public class NodeDto {
    private UUID id;
    private String label;
    private Double x;
    private Double y;
    private UUID electrocardiographyId;  // ID вместо полной сущности
}
