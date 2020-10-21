package TimeManager.repositories;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * Common base repository for all other repositories. Provides basic methods for
 * loading, saving and removing entities.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 *
 * @param <T> The domain type this repository manages.
 * @param <ID> The type of the id of the entity this repository manages.
 */
@NoRepositoryBean
public interface AbstractRepository<T, ID extends Serializable> extends Repository<T, ID> {

    /**
     * Deletes an entity.
     *
     * @param entity The entity to be deleted.
     * @throws IllegalArgumentException If the given entity is (@literal null}.
     */
    void delete(T entity);

    /**
     * Returns all instances of the type.
     *
     * @return All entities.
     */
    List<T> findAll();

    /**
     * Retrieves an entity by its id.
     *
     * @param id must not be {@literal null}.
     * @return The entity with the given id or {@literal null} if none found.
     * @throws IllegalArgumentException If {@code id} is {@literal null}.
     */
    T findById(ID id);

    /**
     * Saves a given entity. Use the returned instance for further operations as
     * the save operation might have changed the entity instance completely.
     *
     * @param <S> The actual domain type if the entity.
     * @param entity The entity to be saved or updated.
     * @return The saved entity.
     */
    <S extends T> S save(S entity);
}
