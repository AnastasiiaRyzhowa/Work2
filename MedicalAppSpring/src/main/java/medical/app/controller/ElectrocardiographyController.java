package medical.app.controller;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import medical.app.entity.Electrocardiography;
import medical.app.entity.dto.ElectrocardiographyDataDto;
import medical.app.entity.dto.ElectrocardiographyDto;
import medical.app.entity.dto.ShortElectrocardiographyDto;
import medical.app.services.ElectrocardiographyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

/**
 * Класс ElectrocardiographyController является контроллером приложения. Внутри контроллера описаны методы с
 * помощью который происходит общение клиента и сервера
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/electrocardiography")
public class ElectrocardiographyController {

    private final ElectrocardiographyService electrocardiographyService;

    @PostMapping
    public ResponseEntity<Electrocardiography> addElectrocardiography(@RequestBody Electrocardiography electrocardiography) {
        Electrocardiography created = electrocardiographyService.saveElectrocardiography(electrocardiography);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/create/person/{id}")
    public ResponseEntity<ElectrocardiographyDto> addElectrocardiographyForPerson(@PathVariable UUID id, @RequestBody Electrocardiography electrocardiography) {
        ElectrocardiographyDto created = electrocardiographyService.saveElectrocardiographyForPerson(id, electrocardiography);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<ElectrocardiographyDto>> getAllElectrocardiographies() {
        List<ElectrocardiographyDto> electrocardiographies = electrocardiographyService.getAllElectrocardiographies();
        return ResponseEntity.ok(electrocardiographies);
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<List<ElectrocardiographyDto>> getAllElectrocardiographiesByPerson(@PathVariable UUID id) {
        List<ElectrocardiographyDto> electrocardiographies = electrocardiographyService.getAllElectrocardiographiesByPerson(id);
        return ResponseEntity.ok(electrocardiographies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ElectrocardiographyDataDto> getElectrocardiography(@PathVariable UUID id) {
        return ResponseEntity.ok(electrocardiographyService.getById(id));
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteElectrocardiography(@PathVariable UUID id) {
        System.out.println("@@@@@@@@@@@@@@@");
        System.out.println(id);
        electrocardiographyService.deleteElectrocardiography(id);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PostMapping("/update/data/{electId}")
    public ResponseEntity<Void> updateCompany(@PathVariable("electId") UUID electId, @RequestBody ShortElectrocardiographyDto electDataDto) {
        electrocardiographyService.updateAllDate(electDataDto, electId);
        return new ResponseEntity<Void>( HttpStatus.OK);
    }

}