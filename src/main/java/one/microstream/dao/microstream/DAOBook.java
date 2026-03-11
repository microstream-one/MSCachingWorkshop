package one.microstream.dao.microstream;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.eclipsestore.RootProvider;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import one.microstream.dao.microstream.postgres.PostDAOBook;
import one.microstream.domain.indices.BookIndices;
import one.microstream.domain.microstream.Book;
import one.microstream.domain.microstream.Company;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.eclipse.store.gigamap.jvector.VectorIndex;
import org.eclipse.store.gigamap.jvector.VectorIndices;
import org.eclipse.store.gigamap.jvector.VectorSearchResult;
import org.eclipse.store.gigamap.types.GigaIterator;
import org.eclipse.store.storage.types.StorageManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DAOBook
{
	@Inject
	RootProvider<Company> company;
	@Inject
	StorageManager storageManager;
	@Inject
	PostDAOBook postDAOBook;

    public List<Book> pageBooks(@NonNull @NotBlank int limit)
    {
        try(Stream<Book> stream = company.root().getGigaBooks().query().stream())
        {
            return stream.limit(limit).collect(Collectors.toList());
        }
    }

    public List<Book> searchBooks(float[] filter, Float score)
    {
        VectorIndices<Book> keyValues = company.root().gigaBooks.index().get(VectorIndices.Category());
        VectorIndex<Book> index = keyValues.iterator().next().value();

        VectorSearchResult<Book> result = index.search(filter, 100);

        result.forEach(entry ->
        {
            final Book similar = entry.entity();
            System.out.printf("%s, score=%s%n", similar.getTitle(), entry.score());
        });

        return result.stream().filter(e -> e.score() >= score)
                .map(vsr -> vsr.entity()).collect(Collectors.toUnmodifiableList());
    }

    public Optional<Book> byPostId(Integer postId)
    {
        return company.root().getGigaBooks().query(BookIndices.postgresIdIndex.is(postId)).findFirst();
    }

	public void insert(final Book book)
	{
		this.company.root().getGigaBooks().add(book);
        this.company.root().getGigaBooks().store();
	}

	public void update(final Book book)
	{
        Optional<Book> optBook = byPostId(book.getPostId());

        if(optBook.isPresent())
        {
            Book bookToChange = optBook.get();

            this.company.root().getGigaBooks().update(bookToChange, p ->
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
            });
            this.company.root().getGigaBooks().store();
        }
	}

	public void deleteByPostId(final Integer postId)
	{
        Optional<Book> optBook = byPostId(postId);

        if(optBook.isPresent())
        {
            Book bookToDelete = optBook.get();

            this.company.root().getGigaBooks().remove(bookToDelete);
            this.company.root().getGigaBooks().store();
        }
	}

}
