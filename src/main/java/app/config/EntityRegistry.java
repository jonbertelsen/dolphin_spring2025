package app.config;

import app.entities.Fee;
import app.entities.Person;
import app.entities.PersonDetail;
import org.hibernate.cfg.Configuration;

final class EntityRegistry {

    private EntityRegistry() {}

    static void registerEntities(Configuration configuration) {
        configuration.addAnnotatedClass(Person.class);
        configuration.addAnnotatedClass(Fee.class);
        configuration.addAnnotatedClass(PersonDetail.class);
        // TODO: Add more entities here...
    }
}