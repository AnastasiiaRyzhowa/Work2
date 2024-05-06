package medical.app.medicalappfx.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class PersonDto {
    private UUID id;

    private String fullName;

    private LocalDate dateOfBirth;
    private String address;

    //    Getter and setter area
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
