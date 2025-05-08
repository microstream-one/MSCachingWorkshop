package one.microstream.dao.microstream;

import io.micronaut.eclipsestore.RootProvider;
import jakarta.inject.Inject;
import one.microstream.domain.microstream.Book;
import one.microstream.domain.microstream.Company;
import org.eclipse.store.storage.types.StorageManager;
import org.postgresql.PGNotification;

import java.util.List;

public class DAOBook
{
	@Inject
	RootProvider<Company> company;
	@Inject
	StorageManager storageManager;

	public void insert(final PGNotification notification)
	{
		// TODO: Implementation of updating an existing book
	}

	public List<Book> findAll()
	{
		return this.company.root().getBooks();
	}

	public void insert(final Book book)
	{
		this.company.root().getBooks().add(book);
		this.storageManager.store(book);
	}
}
