package one.microstream.repositories;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import one.microstream.domain.postgres.PostBook;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepoBook extends CrudRepository<PostBook, Integer>
{
    List<PostBook> findByTitle(String title);
    @NonNull Optional<PostBook> findById(Integer id);
    void deleteById(Integer id);
}
