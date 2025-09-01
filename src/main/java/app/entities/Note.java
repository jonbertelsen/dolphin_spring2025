package app.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder @ToString
@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String note;
    @Column(nullable = false)
    private String createdBy;

    @ManyToOne
    @Setter
    @ToString.Exclude
    private Person person;
}
