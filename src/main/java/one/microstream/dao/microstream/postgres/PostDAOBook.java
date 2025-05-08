package one.microstream.dao.microstream.postgres;

import jakarta.inject.Inject;
import one.microstream.core.mapper.MapperBook;
import one.microstream.domain.microstream.Book;
import one.microstream.domain.postgres.PostBook;
import one.microstream.dto.DtoBook;
import one.microstream.repositories.RepoBook;

public class PostDAOBook
{
    @Inject
    RepoBook repoBook;
    @Inject
    MapperBook mapperBook;

    public PostBook insert(DtoBook book)
    {
        PostBook newPostBook = mapperBook.toNewPostBook(book);
        PostBook saved = repoBook.save(newPostBook);
        return saved;
    }

}
