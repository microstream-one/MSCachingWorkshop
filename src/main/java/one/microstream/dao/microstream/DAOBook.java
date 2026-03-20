package one.microstream.dao.microstream;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.eclipsestore.RootProvider;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import one.microstream.domain.indices.BookIndices;
import one.microstream.domain.microstream.Book;
import one.microstream.domain.microstream.Company;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.WildcardQuery;
import org.eclipse.datagrid.cluster.nodelibrary.types.ClusterLockScope;
import org.eclipse.serializer.concurrency.LockedExecutor;
import org.eclipse.store.gigamap.lucene.LuceneIndex;

@Singleton
public class DAOBook extends ClusterLockScope
{
    private final Company root;
    private final LuceneIndex<Book> luceneIndex;

    public DAOBook(final LockedExecutor lockedExecutor, final RootProvider<Company> rootProvider)
    {
        super(lockedExecutor);
        this.root = rootProvider.root();
        //noinspection unchecked
        this.luceneIndex = rootProvider.root().gigaBooks.index().get(LuceneIndex.class);
    }

    public List<Book> pageBooks(@NonNull @NotBlank int limit)
    {
        return this.read(() ->
        {
            try (final var stream = this.root.getGigaBooks().query().stream())
            {
                return stream.limit(limit).collect(Collectors.toList());
            }
        });
    }

    public Optional<Book> byPostId(final Integer postId)
    {
        return this.read(() -> this.root
            .getGigaBooks()
            .query(BookIndices.POST_ID.is(postId))
            .findFirst());
    }

    public List<Book> findByTitle(final String titleSearch)
    {
        final var query = new WildcardQuery(new Term("title", "*%s*".formatted(titleSearch)));
        return this.read(() -> this.luceneIndex.query(query));
    }

    public Optional<Book> findByIsbn(final String isbn)
    {
        return this.read(() -> this.root
            .gigaBooks
            .query(BookIndices.ISBN.is(isbn))
            .findFirst());
    }

    public List<String> findAllIsbn()
    {
        return this.read(() ->
        {
            try (final var stream = this.root.gigaBooks.query().stream())
            {
                return stream.map(Book::getIsbn).toList();
            }
        });
    }

    public void insert(final Book book)
    {
        this.write(() ->
        {
            this.root.getGigaBooks().add(book);
            this.root.getGigaBooks().store();
        });
    }

    public void update(final Book findBook)
    {
        this.write(() -> this.byPostId(findBook.getPostId()).ifPresent(book ->
        {
            this.root.getGigaBooks().update(
                book, p ->
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
            this.root.getGigaBooks().store();
        }));
    }

    public void deleteByPostId(final Integer postId)
    {
        this.write(() -> this.byPostId(postId).ifPresent(book ->
        {
            this.root.getGigaBooks().remove(book);
            this.root.getGigaBooks().store();
        }));
    }
}
