package medical.app.medicalappfx.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public class ElectrocardiographyDataDto {
    public UUID id;
    public LocalDateTime appointmentDateTime;
    public String data;
    public List<NodeDto> nodes;

    //Getter and Setter area


    public List<NodeDto> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeDto> nodes) {
        this.nodes = nodes;
    }

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "ElectrocardiographyDataDto{" +
                "id=" + id +
                ", appointmentDateTime=" + appointmentDateTime +
                ", nodes=" + nodes +
                '}';
    }
}
