package medical.app.medicalappfx.dto;

import java.time.LocalDateTime;

public class ElectCreateDto {
    public LocalDateTime appointmentDateTime;

    public String data;

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
