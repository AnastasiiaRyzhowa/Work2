package medical.app.mapper;

import medical.app.entity.Person;
import medical.app.entity.dto.PersonDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс с помощью которого происходит преобразование Entity в DTO
 */
@Component
public class PersonMapper {

    /**
     * Преобразование Entity в DTO
     */
    public PersonDto personToPersonDto(Person entity) {
        PersonDto dto = new PersonDto();
        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setAddress(entity.getAddress());
        return dto;
    }

    /**
     * Преобразование списка entity в список dto
     */
    public List<PersonDto> listPersonToListPersonDto(List<Person> entityList) {
        List<PersonDto> dtoList = new ArrayList<>();

        for (Person elem : entityList) {
            dtoList.add(personToPersonDto(elem));
        }

        return dtoList;
    }
}
