package medical.app.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DTO - PersonDto необходима для передачи данных клиенту и общения с ним
 */
@Getter
@Setter
public class PersonDto {
    private UUID id;
    private String fullName;
    private LocalDate dateOfBirth;
    private String address;
}
