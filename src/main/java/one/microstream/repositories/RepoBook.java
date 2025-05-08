package one.microstream.repositories;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import one.microstream.domain.postgres.PostBook;

@Repository
public interface RepoBook extends CrudRepository<PostBook, Integer>
{
}
