package medical.app.medicalappfx.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ShortElectrocardiographyDto {
    public List<ShortNodeDto> nodes;

    public ShortElectrocardiographyDto(List<ShortNodeDto> nodes) {
        this.nodes = nodes;
    }


    public List<ShortNodeDto> getNodes() {
        return nodes;
    }

    public void setNodes(List<ShortNodeDto> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return "ShortElectrocardiographyDto{" +
                "nodes=" + nodes +
                '}';
    }
}
