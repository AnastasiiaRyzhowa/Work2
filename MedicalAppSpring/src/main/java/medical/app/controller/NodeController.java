package medical.app.controller;

import lombok.AllArgsConstructor;
import medical.app.entity.Node;
import medical.app.services.NodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Класс NodeController является контроллером приложения. Внутри контроллера описаны методы с
 * помощью который происходит общение клиента и сервера
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/nodes")
public class NodeController {
    private NodeService nodeService;

    @GetMapping
    public List<Node> getAllNodes() {
        return nodeService.findAllNodes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Node> getNodeById(@PathVariable UUID id) {
        return nodeService.findNodeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Node createNode(@RequestBody Node node) {
        return nodeService.saveNode(node);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Node> updateNode(@PathVariable UUID id, @RequestBody Node node) {
        return nodeService.findNodeById(id)
                .map(existingNode -> {
                    existingNode.setLabel(node.getLabel());
                    existingNode.setX(node.getX());
                    existingNode.setY(node.getY());
                    Node updatedNode = nodeService.saveNode(existingNode);
                    return ResponseEntity.ok(updatedNode);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNode(@PathVariable UUID id) {
        return nodeService.findNodeById(id)
                .map(node -> {
                    nodeService.deleteNode(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
