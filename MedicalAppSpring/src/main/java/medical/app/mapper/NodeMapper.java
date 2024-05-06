package medical.app.mapper;

import medical.app.entity.Electrocardiography;
import medical.app.entity.Node;
import medical.app.entity.dto.NodeDto;
import org.springframework.stereotype.Component;

/**
 * Класс с помощью которого происходит преобразование Entity в DTO
 */
@Component
public class NodeMapper {
    /**
     * Преобразование Entity в DTO
     */
    public NodeDto nodeToNodeDto(Node node) {
        NodeDto nodeDto = new NodeDto();
        nodeDto.setId(node.getId());
        nodeDto.setLabel(node.getLabel());
        nodeDto.setX(node.getX());
        nodeDto.setY(node.getY());
        nodeDto.setElectrocardiographyId(node.getElectrocardiography().getId());
        return nodeDto;
    }
}
