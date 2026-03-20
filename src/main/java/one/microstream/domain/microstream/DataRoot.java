package one.microstream.domain.microstream;

import java.util.HashMap;
import java.util.Map;

import one.microstream.core.lucene.BookDocumentPopulator;
import one.microstream.domain.indices.BookIndices;
import org.eclipse.store.gigamap.lucene.LuceneContext;
import org.eclipse.store.gigamap.lucene.LuceneIndex;
import org.eclipse.store.gigamap.types.GigaMap;

public class DataRoot
{
    private final GigaMap<Book> gigaBooks = GigaMap.<Book>Builder()
        .withBitmapIndex(BookIndices.POST_ID)
        .withBitmapIndex(BookIndices.ISBN)
        .build();
    private Map<byte[], byte[]> debeziumOffsetStore = new HashMap<>();

    public DataRoot()
    {
        this.gigaBooks.index().register(LuceneIndex.Category(LuceneContext.New(new BookDocumentPopulator())));
    }

    public GigaMap<Book> getGigaBooks()
    {
        return this.gigaBooks;
    }

    public Map<byte[], byte[]> getDebeziumOffsetStore()
    {
        return this.debeziumOffsetStore;
    }

    public void setDebeziumOffsetStore(final Map<byte[], byte[]> debeziumOffsetStore)
    {
        this.debeziumOffsetStore = debeziumOffsetStore;
    }
}
