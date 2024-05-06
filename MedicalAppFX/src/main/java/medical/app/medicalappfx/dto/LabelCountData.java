package medical.app.medicalappfx.dto;

public class LabelCountData {
    private String label;
    private Integer count;

    public LabelCountData(String label, Integer count) {
        this.label = label;
        this.count = count;
    }

    public String getLabel() {
        return label;
    }

    public Integer getCount() {
        return count;
    }
}
