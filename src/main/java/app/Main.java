package app;

import app.config.HibernateConfig;
import app.daos.DolphinDAO;
import app.dtos.NotePersonDTO;
import app.dtos.NotePersonRecordDTO;
import app.entities.Fee;
import app.entities.Note;
import app.entities.Person;
import app.entities.PersonDetail;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello Dolphin!");

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        DolphinDAO dao = new DolphinDAO(emf);

        Person p1 = Person.builder().name("Hanzi").build();
        PersonDetail pd1 = new PersonDetail("Algade 3", 4300, "Holbæk", 45);
        p1.addPersonDetail(pd1);
        Fee f1 = Fee.builder().amount(125).payDate(LocalDate.of(2023, 8, 25)).build();
        Fee f2 = Fee.builder().amount(150).payDate(LocalDate.of(2023, 7, 19)).build();
        p1.addFee(f1);
        p1.addFee(f2);
        Note n1 = Note.builder()
                .note("This is very important")
                .createdBy("Bill")
                .build();
        Note n2 = Note.builder()
                .note("There once upon a time")
                .createdBy("Peggy")
                .build();
        p1.addNote(n1);
        p1.addNote(n2);

        dao.create(p1);
        System.out.println(p1.toString());

        List<NotePersonRecordDTO> notes = dao.getNotesAndPersonInfo();

        notes.forEach(n -> System.out.printf("%s er %d gammel og har skrevet noten: %s%n",
                n.personName(), n.personAge(), n.note() ) );

        NotePersonRecordDTO n = notes.get(0);

        emf.close();
    }
}