package app.daos;

import app.entities.Person;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;
import java.util.Optional;

public class DolphinDAO implements IDAO<Person, Integer> {

    private EntityManagerFactory emf;

    public DolphinDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Person create(Person person) {
        try (EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
        } catch (PersistenceException e){
            throw new ApiException(500, e.getMessage());
        }
        return person;
    }

    @Override
    public Optional<Person> getById(Integer id) {
        try (EntityManager em = emf.createEntityManager()){
            Person person = em.find(Person.class, id);
            return Optional.ofNullable(person);
        }
    }

    @Override
    public List<Person> getAll() {
        try (EntityManager em = emf.createEntityManager()){
            TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
            return query.getResultList();
        }
    }

    @Override
    public Person update(Person person) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Person merged = em.merge(person); // returns the managed entity
            em.getTransaction().commit();
            return merged;
        } catch (PersistenceException e) {
            throw new ApiException(500, e.getMessage());
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Person person = em.find(Person.class, id);
            if (person != null) {
                em.remove(person);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().commit();
            return false;
        }
    }

    public Optional<Long> getTotalFeeByPersonId(Integer id){
        try (EntityManager em = emf.createEntityManager()){
            TypedQuery<Long> query = em.createQuery("SELECT sum(f.amount) FROM Fee f Where f.person.id = :id", Long.class);
            query.setParameter("id", id);
            Long result = query.getSingleResult();
            return Optional.ofNullable(result);
        }
    }
}
