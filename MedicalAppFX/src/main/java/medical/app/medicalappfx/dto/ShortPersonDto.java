package medical.app.medicalappfx.dto;

import java.time.LocalDate;
import java.util.UUID;

public class ShortPersonDto {


    public String fullName;

    public LocalDate dateOfBirth;
    public String address;


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
