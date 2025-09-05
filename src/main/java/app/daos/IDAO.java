package app.daos;

import java.util.List;
import java.util.Optional;

public interface IDAO<T, I> {
    T create(T t);
    T getById(I i);
    List<T> getAll();
    T update(T t);
    boolean delete(I i);
}
