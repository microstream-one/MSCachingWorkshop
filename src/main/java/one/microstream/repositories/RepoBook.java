package one.microstream.repositories;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import one.microstream.domain.microstream.Book;

@Repository
public interface RepoBook extends CrudRepository<Book, Integer>
{
}
