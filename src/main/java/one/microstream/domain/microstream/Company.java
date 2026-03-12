package one.microstream.domain.microstream;

import java.util.HashMap;
import java.util.Map;

import one.microstream.domain.indices.BookIndices;
import org.eclipse.store.gigamap.types.BitmapIndices;
import org.eclipse.store.gigamap.types.GigaMap;


public class Company
{
    public GigaMap<Book> gigaBooks	= GigaMap.New();
    public Map<byte[], byte[]> debeziumOffsetStore = new HashMap<>();

    public Company()
    {
        super();

        final BitmapIndices<Book> indices = gigaBooks.index().bitmap();
        indices.add(BookIndices.postgresIdIndex);
        indices.add(BookIndices.ISBNIndex);
        indices.add(BookIndices.TitleIndex);
    }

    public GigaMap<Book> getGigaBooks() {
        return gigaBooks;
    }

    public Map<byte[], byte[]> getDebeziumOffsetStore() {
        return debeziumOffsetStore;
    }
}
