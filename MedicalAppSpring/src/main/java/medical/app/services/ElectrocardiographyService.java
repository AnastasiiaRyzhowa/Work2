package medical.app.services;

import lombok.AllArgsConstructor;
import medical.app.entity.Electrocardiography;
import medical.app.entity.Node;
import medical.app.entity.Person;
import medical.app.entity.dto.*;
import medical.app.exeption.NotFoundException;
import medical.app.mapper.ElectrocardiographyMapper;
import medical.app.repositories.ElectrocardiographyRepository;
import medical.app.repositories.NodeRepository;
import medical.app.repositories.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Класс Сервис ElectrocardiographyService необходим для описания методов работы с данными полученными от клиента или с базы
 */
@AllArgsConstructor
@Service
public class ElectrocardiographyService {
    private final ElectrocardiographyRepository electrocardiographyRepository;
    private final PersonRepository personRepository;
    private final ElectrocardiographyMapper mapper;
    private final NodeRepository nodeRepository;

    public Electrocardiography saveElectrocardiography(Electrocardiography electrocardiography) {
        return electrocardiographyRepository.save(electrocardiography);
    }

    public ElectrocardiographyDto saveElectrocardiographyForPerson(UUID id, Electrocardiography dto) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("person with id:[" + id + "] " +
                "does not found"));

        Electrocardiography newElect = new Electrocardiography();
        newElect.setData(dto.getData());
        newElect.setAppointmentDateTime(dto.getAppointmentDateTime());
        newElect.setPerson(person);


        return mapper.electToElectDto(electrocardiographyRepository.save(newElect));
    }

    public ElectrocardiographyDataDto getById(UUID id) {
        Electrocardiography elect = electrocardiographyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Electrocardiography with id:[" + id + "] " +
                "does not found"));
        return mapper.electToElectDataDto(elect);
    }

    public List<ElectrocardiographyDto> getAllElectrocardiographies() {
        return mapper.listElectToListElectDto(electrocardiographyRepository.findAll());
    }

    public List<ElectrocardiographyDto> getAllElectrocardiographiesByPerson(UUID id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Electrocardiography with Person id:[" + id + "] " +
                        "does not found"));
        return mapper.listElectToListElectDto(electrocardiographyRepository.findAllByPerson(person));
    }

    public void deleteElectrocardiography(UUID id) {
        electrocardiographyRepository.deleteById(id);
    }

    public void updateAllDate(ShortElectrocardiographyDto electDataDto, UUID electId) {
        Electrocardiography oldElect = electrocardiographyRepository.findById(electId)
                .orElseThrow(() -> new NotFoundException("Electrocardiography with id:[" + electId + "] " +
                        "does not found"));

        nodeRepository.deleteAllByElectrocardiography(oldElect);

        Electrocardiography oldElect2 = electrocardiographyRepository.findById(electId)
                .orElseThrow(() -> new NotFoundException("Electrocardiography with id:[" + electId + "] " +
                        "does not found"));
//
        for (ShortNodeDto elem : electDataDto.getNodes()) {
            Node node = new Node();
            node.setLabel(elem.getLabel());
            node.setX(elem.getX());
            node.setY(elem.getY());
            node.setElectrocardiography(oldElect2);
            nodeRepository.save(node);
        }

//        return mapper.electToElectDataDto(oldElect2);
    }
}
