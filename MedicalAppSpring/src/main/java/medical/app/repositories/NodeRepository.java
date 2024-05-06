package medical.app.repositories;

import medical.app.entity.Electrocardiography;
import medical.app.entity.Node;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для Node
 */
@Repository
public interface NodeRepository extends CrudRepository<Node, UUID> {
    List<Node> findAll();

    void deleteAllByElectrocardiography(Electrocardiography elect);

    void deleteByElectrocardiographyId(UUID electrocardiographyId);

}
