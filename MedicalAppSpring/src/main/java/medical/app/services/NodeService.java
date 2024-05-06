package medical.app.services;

import lombok.AllArgsConstructor;
import medical.app.entity.Node;
import medical.app.repositories.NodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
/**
 * Класс Сервис NodeService необходим для описания методов работы с данными полученными от клиента или с базы
 */
@AllArgsConstructor
@Service
public class NodeService {

    private NodeRepository nodeRepository;

    public List<Node> findAllNodes() {
        return nodeRepository.findAll();
    }

    public Optional<Node> findNodeById(UUID id) {
        return nodeRepository.findById(id);
    }

    public Node saveNode(Node node) {
        return nodeRepository.save(node);
    }

    public void deleteNode(UUID id) {
        nodeRepository.deleteById(id);
    }
}
