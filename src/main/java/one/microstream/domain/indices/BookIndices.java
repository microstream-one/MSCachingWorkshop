package one.microstream.domain.indices;

import jakarta.inject.Singleton;
import one.microstream.core.ollama.EmbeddingService;
import one.microstream.domain.microstream.Book;
import one.microstream.domain.microstream.Company;
import org.eclipse.store.gigamap.jvector.VectorIndex;
import org.eclipse.store.gigamap.jvector.VectorIndexConfiguration;
import org.eclipse.store.gigamap.jvector.VectorIndices;
import org.eclipse.store.gigamap.jvector.VectorSimilarityFunction;
import org.eclipse.store.gigamap.types.GigaMap;
import org.eclipse.store.gigamap.types.IndexerInteger;
import org.eclipse.store.gigamap.types.IndexerString;

@Singleton
public class BookIndices
{
    public static VectorIndex<Book> index = null;

    public final static IndexerInteger<one.microstream.domain.microstream.Book> postgresIdIndex = new IndexerInteger.Abstract<Book>()
    {
        public String name()
        {
            return "postId";
        }

        @Override
        public Integer getInteger(final Book entity)
        {
            return entity.getPostId();
        }
    };

    public final static IndexerString<one.microstream.domain.microstream.Book> TitleIndex = new IndexerString.Abstract<Book>()
    {
        public String name()
        {
            return "title";
        }

        @Override
        public String getString(final Book entity)
        {
            return entity.getTitle();
        }
    };

    public static void registerVectorIndex(GigaMap<Book> map)
    {
        VectorIndices<Book> vectorIndices = map.index().register(VectorIndices.Category());

        // Configure the vector index
        VectorIndexConfiguration config = VectorIndexConfiguration.builder()
                .dimension(384)
                .similarityFunction(VectorSimilarityFunction.COSINE)
                .build();

        index = vectorIndices.add("embeddings", config, new EmbeddingService());
    }
}
