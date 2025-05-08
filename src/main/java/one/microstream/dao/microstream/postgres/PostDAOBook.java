package one.microstream.dao.microstream.postgres;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import one.microstream.core.mapper.MapperBook;
import one.microstream.domain.postgres.PostBook;
import one.microstream.dto.DtoBook;
import one.microstream.repositories.RepoBook;

import javax.swing.text.html.StyleSheet;
import java.util.List;

@Singleton
public class PostDAOBook
{
    @Inject
    RepoBook repoBook;
    @Inject
    MapperBook mapperBook;

    public PostBook insert(final DtoBook book)
    {
        final PostBook saved = this.repoBook.save(this.mapperBook.toNewPostBook(book));
        return saved;
    }

    public List<PostBook> findAll()
    {
        return this.repoBook.findAll();
    }

}
