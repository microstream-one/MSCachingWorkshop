package one.microstream.domain.microstream;

import java.util.HashMap;
import java.util.Map;

import one.microstream.core.lucene.BookDocumentPopulator;
import one.microstream.core.ollama.EmbeddingService;
import one.microstream.domain.indices.BookIndices;
import org.eclipse.store.gigamap.jvector.VectorIndex;
import org.eclipse.store.gigamap.jvector.VectorIndexConfiguration;
import org.eclipse.store.gigamap.jvector.VectorIndices;
import org.eclipse.store.gigamap.jvector.VectorSimilarityFunction;
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
        // register lucene index
        this.gigaBooks.index().register(LuceneIndex.Category(LuceneContext.New(new BookDocumentPopulator())));
        // register vector index
        final var vectorIndices = this.gigaBooks.index().register(VectorIndices.Category());
        final var vectorIndexConfig = VectorIndexConfiguration.builder()
            .dimension(768)
            .similarityFunction(VectorSimilarityFunction.COSINE)
            .build();
        vectorIndices.add("embeddings", vectorIndexConfig, new EmbeddingService());
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
