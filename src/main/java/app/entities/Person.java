package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    // Relationer 1:1

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
    private PersonDetail personDetail;

    // Relationer 1:m

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    @Builder.Default  // <---- This one is necessary with @Builder
    private Set<Fee> fees = new HashSet<>();

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    @Builder.Default  // <---- This one is necessary with @Builder
    private Set<Note> notes = new HashSet<>();

    // Bi-directional update

    public void addPersonDetail(PersonDetail personDetail) {
        this.personDetail = personDetail;
        if (personDetail != null) {
            personDetail.setPerson(this);
        }
    }

    public void addFee(Fee fee) {
        this.fees.add(fee);
        if (fee != null) {
            fee.setPerson(this);
        }
    }

    public void addNote(Note note) {
        this.notes.add(note);
        if (note != null) {
            note.setPerson(this);
        }
    }

}
