package one.microstream.dao.microstream;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.http.HttpResponse;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import one.microstream.dao.microstream.postgres.PostDAOBook;
import one.microstream.domain.microstream.Book;
import one.microstream.domain.microstream.Company;
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

	public void insert(final Book book)
	{
		this.company.root().getGigaBooks().add(book);
		this.storageManager.store(book);
	}

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
				this.storageManager.store(found);
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
