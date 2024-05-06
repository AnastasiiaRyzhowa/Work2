package medical.app.services;

import lombok.AllArgsConstructor;
import medical.app.entity.Person;
import medical.app.entity.dto.PersonDto;
import medical.app.entity.dto.ShortPersonDto;
import medical.app.exeption.NotFoundException;
import medical.app.mapper.PersonMapper;
import medical.app.repositories.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Класс Сервис PersonService необходим для описания методов работы с данными полученными от клиента или с базы
 */
@Service
@AllArgsConstructor
public class PersonService {

    private PersonRepository personRepository;
    private final PersonMapper personMapper;

    public List<PersonDto> findByFullName(String fullName) {
        return personMapper.listPersonToListPersonDto(personRepository.findByFullName(fullName));
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public List<PersonDto> getShortPersonDtoList() {
        return personMapper.listPersonToListPersonDto(personRepository.findAll());

    }

    public Optional<Person> findById(UUID id) {
        return personRepository.findById(id);
    }

    public PersonDto save(ShortPersonDto dto) {
        Person entity = new Person();
        entity.setFullName(dto.getFullName());
        entity.setDateOfBirth(dto.getDateOfBirth());
        entity.setAddress(dto.getAddress());
        return personMapper.personToPersonDto(personRepository.save(entity));
    }

    public void deleteById(UUID id) {
        personRepository.deleteById(id);
    }

    public PersonDto update(UUID id, ShortPersonDto dto) {
        Person oldPerson = personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Person with id:[" + id + "] does not found"));
        oldPerson.setFullName(dto.getFullName());
        oldPerson.setAddress(dto.getAddress());
        oldPerson.setDateOfBirth(dto.getDateOfBirth());

        Person newPerson = personRepository.save(oldPerson);
        return personMapper.personToPersonDto(newPerson);
    }
}