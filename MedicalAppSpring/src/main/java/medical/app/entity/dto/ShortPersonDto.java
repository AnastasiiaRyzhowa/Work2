package medical.app.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO - ShortPersonDto необходима для передачи данных клиенту и общения с ним
 */
@Getter
@Setter
public class ShortPersonDto {
    private String fullName;
    private LocalDate dateOfBirth;
    private String address;
}
