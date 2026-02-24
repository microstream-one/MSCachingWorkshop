package one.microstream.repositories;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import one.microstream.domain.postgres.PostBook;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RepoBook extends CrudRepository<PostBook, Integer>
{
    List<PostBook> findByTitle(String title);
    List<PostBook> findByCreatedAfter(LocalDateTime from);
    List<PostBook> findByUpdatedAfter(LocalDateTime from);
}
