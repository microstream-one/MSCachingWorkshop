package one.microstream.dao.microstream;

import io.micronaut.eclipsestore.RootProvider;
import jakarta.inject.Inject;
import one.microstream.domain.microstream.Book;
import one.microstream.domain.microstream.Company;
import org.eclipse.store.storage.types.StorageManager;

import java.util.List;

public class DAOBook
{
    @Inject
    RootProvider<Company> company;
    @Inject
    StorageManager storageManager;

    public List<Book> findAll()
    {
        return company.root().getBooks();
    }

    public void insert(Book book)
    {
        company.root().getBooks().add(book);
        storageManager.store(book);
    }
}
