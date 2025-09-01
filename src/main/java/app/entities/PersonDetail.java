package app.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Table(name="person_detail")
@Entity
public class PersonDetail {
    @Id
    private Integer id;
    private String Address;
    private int zip;
    private String city;
    private int age;

    // Relationer 1:1

    @OneToOne
    @MapsId
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Person person;

    public PersonDetail(String address, int zip, String city, int age) {
        Address = address;
        this.zip = zip;
        this.city = city;
        this.age = age;
    }
}
