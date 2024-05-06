package medical.app.medicalappfx.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ElectrocardiographyDto {
    public UUID id;
    public LocalDateTime appointmentDateTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }
}
