package one.microstream.dao.microstream;

import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.serde.ObjectMapper;
import jakarta.inject.Inject;
import one.microstream.core.init.DatabaseEvent;
import one.microstream.dao.microstream.postgres.PostDAOBook;
import one.microstream.domain.microstream.Book;
import one.microstream.domain.microstream.Company;
import one.microstream.domain.postgres.PostBook;
import org.eclipse.store.storage.types.Database;
import org.eclipse.store.storage.types.StorageManager;
import org.postgresql.PGNotification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DAOBook
{
	@Inject
	RootProvider<Company> company;
	@Inject
	StorageManager storageManager;
	@Inject
	PostDAOBook postDAOBook;
	@Inject
	ObjectMapper objectMapper;


	public void insert(final PGNotification notification)
	{
		String parameter = notification.getParameter();

        try
		{
			DatabaseEvent created = objectMapper.readValue(parameter, DatabaseEvent.class);

			Book book = new Book();
			book.setPostId(created.getData().getId());
			book.setTitle(created.getData().getTitle());
			book.setAuthor(created.getData().getAuthor());
			book.setPages(created.getData().getPages());
			book.setGenre(created.getData().getGenre());

			company.root().getBooks().add(book);
			storageManager.store(company.root().getBooks());
		}
		catch (IOException e)
		{
            throw new RuntimeException(e);
        }
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

	public void performStartupSync()
	{
		List<PostBook> postAll = postDAOBook.findAll();
		List<Book> msAll = findAll();

		synchronizeLists(msAll, postAll);

		storageManager.store(msAll);
	}

	public static void synchronizeLists(List<Book> listA, List<PostBook> listB)
	{
		if (listA == null || listB == null) {
			throw new IllegalArgumentException("Lists cannot be null");
		}

		// Create copies to avoid modifying the original lists during iteration
		List<Book> copyA = new ArrayList<>(listA);
		List<PostBook> copyB = new ArrayList<>(listB);

		// Remove books from A that aren't in B
		List<Book> toRemove = copyA.stream()
				.filter(bookA -> copyB.stream().noneMatch(bookB -> bookB.getId().equals(bookA.getPostId())))
				.collect(Collectors.toList());

		listA.removeAll(toRemove);

		// Add books to A that are in B but not in A
		List<PostBook> toAdd = copyB.stream()
				.filter(bookB -> copyA.stream().noneMatch(bookA -> bookA.getPostId().equals(bookB.getId())))
				.collect(Collectors.toList());

		listA.addAll(toAdd.stream().map(b ->
				new Book(b.getIsbn(), b.getId(), b.getTitle(), b.getAuthor(), b.getGenre(), b.getPages()))
				.collect(Collectors.toList()));
	}
}
