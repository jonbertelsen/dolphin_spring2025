package app.daos;

import app.config.HibernateConfig;
import app.entities.Note;
import app.entities.Person;
import app.exceptions.ApiException;
import app.populators.PersonPopulator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DolphinDAOTest {

    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private final DolphinDAO dolphinDAO = new DolphinDAO(emf);
    private Person p1;
    private Person p2;
    private Person p3;
    private List<Person> persons;

    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createNativeQuery("TRUNCATE TABLE fee, person_detail, person RESTART IDENTITY CASCADE")
                    .executeUpdate();
            em.getTransaction().commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        persons = PersonPopulator.populatePersons(dolphinDAO);
        if (persons.size() == 3) {
            p1 = persons.get(0);
            p2 = persons.get(1);
            p3 = persons.get(2);
        } else {
            throw new ApiException(500, "Populator doesnt work");
        }
    }

    @AfterAll
    void tearDown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("EntityManagerFactory closed.");
        }
    }

    @Test
    void create() {
        // Arrange
        Person newPerson = Person.builder().name("Diana Douglas").build();

        // Act
        Person created = dolphinDAO.create(newPerson);

        // Assert
        assertNotNull(created.getId(), "Created person should have an ID");
        assertEquals("Diana Douglas", created.getName());

        // And the total count should be 4 (3 from populator + 1 new)
        List<Person> all = dolphinDAO.getAll();
        assertEquals(4, all.size());

    }

    @Test
    void getById() {
        // Act
        Optional<Person> person = dolphinDAO.getById(p1.getId());
        // Assert
        assertThat("Expected person to be present", person.isPresent(), is(true));
        assertThat(person.get(), hasProperty("id", is(p1.getId())));
        assertThat(person.get(), hasProperty("name", is(p1.getName())));
        assertEquals(person.get(), p1);
    }

    @Test
    void getById_missing() {
        Optional<Person> missing = dolphinDAO.getById(-999);
        assertThat(missing.isEmpty(), is(true));
    }

    @Test
    void getAll() {
        // Act
        List<Person> all = dolphinDAO.getAll();
        // Assert
        assertThat(all, is(notNullValue()));
        assertThat(all, hasSize(3));
        assertThat(all, everyItem(hasProperty("id", notNullValue())));
        assertThat(all, containsInAnyOrder(p1, p2, p3));
    }

    @Test
    void update() {
        // Arrange
        String original = p2.getName();
        String updatedName = original + " (updated)";
        Person toUpdate = Person.builder()
                .id(p2.getId())   // ensure we update the existing row
                .name(updatedName)
                .build();

        // Act
        Person merged = dolphinDAO.update(toUpdate);

        // Assert (returned object)
        assertThat(merged, hasProperty("id", is(p2.getId())));
        assertThat(merged, hasProperty("name", is(updatedName)));

        // Assert (read-back consistency)
        Optional<Person> reloaded = dolphinDAO.getById(p2.getId());
        assertThat(reloaded.isPresent(), is(true));
        assertThat(reloaded.get(), hasProperty("name", is(updatedName)));

        // Count unchanged (still 3 from populator)
        assertEquals(3, dolphinDAO.getAll().size());
    }

    @Test
    void update_nonExisting_shouldNotCreateSilently() {
        Person phantom = Person.builder().id(9999).name("Ghost").build();
        // Expect: either exception, or false, or Optional.empty()
        // For example, if you choose to throw:
        assertThrows(ApiException.class, () -> dolphinDAO.update(phantom));
    }

    @Test
    void delete() {
        // Act
        dolphinDAO.delete(p3.getId());

        // Assert: now p3 should be gone
        Optional<Person> afterDelete = dolphinDAO.getById(p3.getId());
        assertThat(afterDelete.isEmpty(), is(true));

        // Count should drop to 2
        List<Person> remaining = dolphinDAO.getAll();
        assertThat(remaining, hasSize(2));
        assertThat(remaining, containsInAnyOrder(p1, p2));
    }

    @Test
    void getTotalFeeByPersonId() {
        // Act
        Long result = dolphinDAO.getTotalFeeByPersonId(p1.getId()).orElse(0L);
        // Assert
        assertEquals(300, result);
    }

    @Test
    void getNotesById() {
        List<Note> expectedNotes = p3.getNotes().stream().toList();
        List<Note> notes = dolphinDAO.getNotesById(p3.getId());
        assertEquals(2, notes.size());
        assertThat(notes, containsInAnyOrder(expectedNotes.get(0), expectedNotes.get(1) ));

    }
}