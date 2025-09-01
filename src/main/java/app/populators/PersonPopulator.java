package app.populators;

import app.daos.DolphinDAO;
import app.entities.Person;
import app.entities.PersonDetail;
import app.entities.Fee;
import app.entities.Note;

import java.util.List;

public class PersonPopulator {
    public static List<Person> populatePersons(DolphinDAO dolphinDAO) {

        // Create first person
        Person p1 = Person.builder()
                .name("Alice Anderson")
                .build();
        p1.addPersonDetail(PersonDetail.builder()
                .Address("123 Main Street")
                        .zip(2000)
                        .city("Frederiksberg C")
                .build());
        p1.addFee(Fee.builder().amount(100).build());
        p1.addFee(Fee.builder().amount(200).build());

        // Create second person
        Person p2 = Person.builder()
                .name("Bob Brown")
                .build();
        p2.addPersonDetail(PersonDetail.builder()
                .zip(2100)
                .city("København Ø")
                .build());
        p2.addFee(Fee.builder().amount(150).build());

        // Create third person
        Person p3 = Person.builder()
                .name("Charlie Chaplin")
                .build();
        p3.addPersonDetail(PersonDetail.builder()
                .zip(4300)
                .city("Holbæk")
                .build());

        Note n1 = Note.builder()
                .note("This is very important")
                .createdBy("Bill")
                .build();
        Note n2 = Note.builder()
                .note("There once upon a time")
                .createdBy("Peggy")
                .build();
        Note n3 = Note.builder()
                .note("More power to the people")
                .createdBy("Bob")
                .build();
        Note n4 = Note.builder()
                .note("Mere kage til folket")
                .createdBy("Ben")
                .build();

        p1.addNote(n1);
        p2.addNote(n2);
        p3.addNote(n3);
        p3.addNote(n4);

        // Persist them using DAO
        dolphinDAO.create(p1);
        dolphinDAO.create(p2);
        dolphinDAO.create(p3);

        return List.of(p1, p2, p3);
    }
}
