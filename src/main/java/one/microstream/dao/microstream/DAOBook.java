package one.microstream.dao.microstream;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.eclipsestore.RootProvider;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import one.microstream.domain.indices.BookIndices;
import one.microstream.domain.microstream.Book;
import one.microstream.domain.microstream.Company;
import org.eclipse.datagrid.cluster.nodelibrary.types.ClusterLockScope;
import org.eclipse.serializer.concurrency.LockedExecutor;

@Singleton
public class DAOBook extends ClusterLockScope
{
    @Inject
    RootProvider<Company> company;

    public DAOBook(final LockedExecutor lockedExecutor)
    {
        super(lockedExecutor);
    }

    public List<Book> pageBooks(@NonNull @NotBlank int limit)
    {
        return this.read(() ->
        {
            try (Stream<Book> stream = company.root().getGigaBooks().query().stream())
            {
                return stream.limit(limit).collect(Collectors.toList());
            }
        });
    }

    public Optional<Book> byPostId(Integer postId)
    {
        return this.read(() -> this.company.root()
            .getGigaBooks()
            .query(BookIndices.postgresIdIndex.is(postId))
            .findFirst());
    }

    public List<Book> findByTitle(final String titleSearch)
    {
        return this.read(() -> this.company.root()
            .gigaBooks
            .query(BookIndices.TitleIndex.containsIgnoreCase(titleSearch))
            .toList());
    }

    public Optional<Book> findByIsbn(final String isbn)
    {
        return this.read(() -> this.company.root()
                .gigaBooks
                .query(BookIndices.ISBNIndex.is(isbn))
                .findFirst());
    }

    public List<String> findAllIsbn()
    {
        return this.read(() ->
        {
            try (final var stream = this.company.root().gigaBooks.query().stream())
            {
                return stream.map(Book::getIsbn).toList();
            }
        });
    }

    public void insert(final Book book)
    {
        this.write(() ->
        {
            this.company.root().getGigaBooks().add(book);
            this.company.root().getGigaBooks().store();
        });
    }

    public void update(final Book book)
    {
        this.write(() ->
        {
            Optional<Book> optBook = byPostId(book.getPostId());

            if (optBook.isPresent())
            {
                Book bookToChange = optBook.get();

                this.company.root().getGigaBooks().update(
                    bookToChange, p ->
                    {
                        p.setTitle(book.getTitle());
                        p.setAuthor(book.getAuthor());
                        p.setGenre(book.getGenre());
                        p.setIsbn(book.getIsbn());
                        p.setPages(book.getPages());
                        p.setPrice(book.getPrice());
                        p.setDescription(book.getDescription());
                        p.setDescription(book.getLanguage());
                        p.setYear(book.getYear());
                        p.setPublisher(book.getPublisher());
                    }
                );
                this.company.root().getGigaBooks().store();
            }
        });
    }

    public void deleteByPostId(final Integer postId)
    {
        this.write(() ->
        {
            Optional<Book> optBook = byPostId(postId);

            if (optBook.isPresent())
            {
                Book bookToDelete = optBook.get();

                this.company.root().getGigaBooks().remove(bookToDelete);
                this.company.root().getGigaBooks().store();
            }
        });
    }
}
