package medical.app.repositories;

import medical.app.entity.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для Person
 */
@Repository
public interface PersonRepository extends CrudRepository<Person, UUID> {
    List<Person> findAll();

    List<Person> findByFullName(String fullName);
}