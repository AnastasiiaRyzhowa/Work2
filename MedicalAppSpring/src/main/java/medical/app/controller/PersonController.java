package medical.app.controller;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import medical.app.entity.Person;
import medical.app.entity.dto.PersonDto;
import medical.app.entity.dto.ShortPersonDto;
import medical.app.services.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Класс PersonController является контроллером приложения. Внутри контроллера описаны методы с
 * помощью который происходит общение клиента и сервера
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private PersonService personService;

    @GetMapping("/search/{fullName}")
    public ResponseEntity<List<PersonDto>> getPersonByFullName(@PathVariable String fullName) {
        return new ResponseEntity<>(personService.findByFullName(fullName.replace("+", " ")), HttpStatus.OK);

    }

    @GetMapping
    public List<PersonDto> getAllShortPersons() {
        return personService.getShortPersonDtoList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable UUID id) {
        return personService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PersonDto> createPerson(@RequestBody ShortPersonDto person) {
        PersonDto savedPerson = personService.save(person);
        return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
    }

    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<PersonDto> updatePerson(@PathVariable UUID id, @RequestBody ShortPersonDto personDetails) {
        PersonDto editPerson = personService.update(id, personDetails);
        return new ResponseEntity<>(editPerson, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable UUID id) {
        return personService.findById(id)
                .map(person -> {
                    personService.deleteById(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}