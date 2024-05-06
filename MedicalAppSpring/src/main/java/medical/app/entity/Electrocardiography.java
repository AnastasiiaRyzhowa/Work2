package medical.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Класс Electrocardiography является сущностью для ЭКГ пациентов. Внутри класса описаны переменные для ЭКГ
 * пациентов
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "electrocardiography")
public class Electrocardiography {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "appointment_date_time")
    private LocalDateTime appointmentDateTime;

    @Column(name = "data", length = 2147483647)
    private String data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @OneToMany(mappedBy = "electrocardiography", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Node> nodes;
}
