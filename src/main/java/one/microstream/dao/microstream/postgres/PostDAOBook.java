package one.microstream.dao.microstream.postgres;

import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import one.microstream.core.mapper.MapperBook;
import one.microstream.domain.postgres.PostBook;
import one.microstream.dto.DtoBook;
import one.microstream.repositories.RepoBook;

import javax.swing.text.html.StyleSheet;
import java.util.List;
import java.util.Optional;

@Singleton
public class PostDAOBook
{
    @Inject
    RepoBook repoBook;
    @Inject
    MapperBook mapperBook;

    public PostBook insert(final DtoBook book)
    {
        return this.repoBook.save(this.mapperBook.toNewPostBook(book));
    }

    public PostBook insert(final PostBook book)
    {
        return this.repoBook.save(book);
    }

    public PostBook update(final DtoBook dto) throws RuntimeException
    {
        Optional<PostBook> byId = repoBook.findById(dto.id());

        if(byId.isPresent())
        {
            return this.repoBook.update(this.mapperBook.updatePostBook(byId.get(), dto));
        }
        else {
            throw new RuntimeException("Book not found");
        }
    }

    public void delete(@NonNull @NotBlank Integer id) {
        this.repoBook.deleteById(id);
    }

    public List<PostBook> findAll()
    {
        return this.repoBook.findAll();
    }


}
