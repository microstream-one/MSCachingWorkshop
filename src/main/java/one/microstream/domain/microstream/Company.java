package one.microstream.domain.microstream;


import jakarta.inject.Singleton;
import one.microstream.domain.indices.BookIndices;
import org.eclipse.store.gigamap.types.BitmapIndices;
import org.eclipse.store.gigamap.types.GigaMap;

@Singleton
public class Company
{
    public GigaMap<Book> gigaBooks	= GigaMap.New();

    public Company()
    {
        super();

        BitmapIndices<Book> indices = gigaBooks.index().bitmap();
        indices.add(BookIndices.postgresIdIndex);

        BookIndices.registerVectorIndex(gigaBooks);
    }

    public GigaMap<Book> getGigaBooks() {
        return gigaBooks;
    }
}
