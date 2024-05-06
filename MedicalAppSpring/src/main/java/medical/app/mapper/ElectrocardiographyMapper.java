package medical.app.mapper;

import lombok.AllArgsConstructor;
import medical.app.entity.Electrocardiography;
import medical.app.entity.Node;
import medical.app.entity.dto.ElectrocardiographyDataDto;
import medical.app.entity.dto.ElectrocardiographyDto;
import medical.app.entity.dto.NodeDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс с помощью которого происходит преобразование Entity в DTO
 */
@AllArgsConstructor
@Component
public class ElectrocardiographyMapper {

    private final NodeMapper nodeMapper;

    /**
     * Преобразование Entity в DTO
     */
    public ElectrocardiographyDto electToElectDto(Electrocardiography entity) {
        ElectrocardiographyDto dto = new ElectrocardiographyDto();
        dto.setId(entity.getId());
        dto.setAppointmentDateTime(entity.getAppointmentDateTime());
        return dto;
    }

    /**
     * Преобразование Entity в DTO
     */
    public ElectrocardiographyDataDto electToElectDataDto(Electrocardiography entity) {
        ElectrocardiographyDataDto dto = new ElectrocardiographyDataDto();
        dto.setId(entity.getId());
        dto.setAppointmentDateTime(entity.getAppointmentDateTime());
        dto.setData(entity.getData());

        List<NodeDto> nodeDto = new ArrayList<>();

        for (Node elem : entity.getNodes()) {
            nodeDto.add(nodeMapper.nodeToNodeDto(elem));
        }

        dto.setNodes(nodeDto);
        return dto;
    }

    /**
     * Преобразование списка entity в список dto
     */
    public List<ElectrocardiographyDto> listElectToListElectDto(List<Electrocardiography> entityList) {
        List<ElectrocardiographyDto> dtoList = new ArrayList<>();
        for (Electrocardiography elem : entityList) {
            dtoList.add(electToElectDto(elem));
        }
        return dtoList;
    }
}
