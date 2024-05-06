package medical.app.entity;

import jakarta.persistence.*;
import jdk.jfr.Label;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Класс Node является сущностью для меток на графике ЭКГ. Внутри класса описаны координаты меток и их названия
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "node")
public class Node {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(name = "label")
    private String label;

    @Column(name = "axios_x")
    private Double x;

    @Column(name = "axios_y")
    private  Double y;

    @ManyToOne
    @JoinColumn(name = "electrocardiography_id", nullable = false)
    private Electrocardiography electrocardiography;
}
