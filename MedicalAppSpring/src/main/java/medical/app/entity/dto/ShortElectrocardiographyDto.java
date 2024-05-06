package medical.app.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * DTO - ShortElectrocardiographyDto необходима для передачи данных клиенту и общения с ним
 */
@Getter
@Setter
@ToString
public class ShortElectrocardiographyDto {
    private List<ShortNodeDto> nodes;
}
