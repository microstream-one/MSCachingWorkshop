package one.microstream.repositories;

import java.util.List;
import java.util.Optional;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.QueryHint;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import one.microstream.domain.postgres.PostBook;
import org.hibernate.jpa.HibernateHints;

@Repository
public interface RepoBook extends CrudRepository<PostBook, Integer>
{
    @QueryHint(name = HibernateHints.HINT_CACHEABLE, value = "true")
    List<PostBook> findByTitleIlike(final String titleSearch);

    @QueryHint(name = HibernateHints.HINT_CACHEABLE, value = "true")
    Optional<PostBook> findByIsbn(final String isbn);

    @NonNull
    Optional<PostBook> findById(Integer id);

    void deleteById(Integer id);
}
