package medical.app.repositories;

import medical.app.entity.Electrocardiography;
import medical.app.entity.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для Electrocardiography
 */
@Repository
public interface ElectrocardiographyRepository extends CrudRepository<Electrocardiography, UUID> {
    List<Electrocardiography> findAll();
    List<Electrocardiography> findAllByPerson(Person person);
}