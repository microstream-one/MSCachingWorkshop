package one.microstream.dao.microstream;

import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.serde.ObjectMapper;
import jakarta.inject.Inject;
import one.microstream.dao.microstream.postgres.PostDAOBook;
import one.microstream.domain.postgres.PostBook;
import org.eclipse.store.storage.types.StorageManager;

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


	public List<Book> findAll()
	{
		//TODO enter some findAll code here
	}

	public void insert(final Book book)
	{
		//TODO enter some insert and store code here
	}
}
