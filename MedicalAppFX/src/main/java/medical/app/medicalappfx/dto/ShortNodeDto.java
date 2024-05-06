package medical.app.medicalappfx.dto;

public class ShortNodeDto {
    public String label;
    public Double x;
    public Double y;

    public ShortNodeDto(String label, Double x, Double y) {
        this.label = label;
        this.x = x;
        this.y = y;
    }

    public ShortNodeDto() {
    }

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
        return "ShortNodeDto{" +
                "label='" + label + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
