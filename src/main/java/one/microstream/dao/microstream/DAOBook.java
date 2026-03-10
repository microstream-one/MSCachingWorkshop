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

    public List<Book> allBooks()
    {
        GigaIterator<Book> iterator = company.root().getGigaBooks().iterator();
        List<Book> collect = Stream.generate(iterator::next).collect(Collectors.toUnmodifiableList());
        return collect;
    }

    public List<Book> searchBooks(float[] filter)
    {
        VectorIndices<Book> keyValues = company.root().gigaBooks.index().get(VectorIndices.Category());
        VectorIndex<Book> index = keyValues.iterator().next().value();

        VectorSearchResult<Book> result = index.search(filter, 100);

        result.forEach(entry ->
        {
            final Book similar = entry.entity();
            System.out.printf("%s, score=%s%n", similar.getTitle(), entry.score());
        });

        return result.stream().filter(e -> e.score() >= 0.80f)
                .map(vsr -> vsr.entity()).collect(Collectors.toUnmodifiableList());
    }

	public void insert(final Book book)
	{
		this.company.root().getGigaBooks().add(book);
        this.company.root().getGigaBooks().store();
	}

    //TODO Improve implementation
	public void update(final Book book)
	{
		try (Stream<Book> stream = this.company.root().getGigaBooks().query().stream())
		{
			final Optional<Book> existing = stream
					.filter(b -> b.getPostId() != null && b.getPostId().equals(book.getPostId()))
					.findFirst();

			if (existing.isPresent())
			{
				final Book found = existing.get();
				found.setTitle(book.getTitle());
				found.setAuthor(book.getAuthor());
				found.setGenre(book.getGenre());
				found.setIsbn(book.getIsbn());
				found.setPages(book.getPages());
                found.setPrice(book.getPrice());
                found.setDescription(book.getDescription());
                found.setDescription(book.getLanguage());
                found.setYear(book.getYear());
                found.setPublisher(book.getPublisher());
                this.company.root().getGigaBooks().store();
			}
			else
			{
				this.insert(book);
			}
		}
	}

	public void deleteByPostId(final Integer postId)
	{
		try (Stream<Book> stream = this.company.root().getGigaBooks().query().stream())
		{
			stream.filter(b -> b.getPostId() != null && b.getPostId().equals(postId))
					.findFirst()
					.ifPresent(book ->
					{
						this.company.root().getGigaBooks().remove(book);
						this.storageManager.store(this.company.root().getGigaBooks());
					});
		}
	}

}
