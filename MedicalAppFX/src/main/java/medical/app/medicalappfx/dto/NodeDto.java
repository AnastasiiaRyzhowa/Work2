package medical.app.medicalappfx.dto;

import java.util.UUID;

public class NodeDto {

    public UUID id;
    public String label;
    public Double x;
    public Double y;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "NodeDto{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
