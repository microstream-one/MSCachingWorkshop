package one.microstream.domain.indices;

import one.microstream.core.ollama.EmbeddingService;
import one.microstream.domain.microstream.Book;
import org.eclipse.store.gigamap.jvector.VectorIndexConfiguration;
import org.eclipse.store.gigamap.jvector.VectorIndices;
import org.eclipse.store.gigamap.jvector.VectorSimilarityFunction;
import org.eclipse.store.gigamap.types.BinaryIndexerInteger;
import org.eclipse.store.gigamap.types.BinaryIndexerString;
import org.eclipse.store.gigamap.types.GigaMap;

public class BookIndices
{
    public final static BinaryIndexerString<Book> ISBN = new BinaryIndexerString.Abstract<>()
    {
        @Override
        public String name()
        {
            return "isbn";
        }

        @Override
        public String getString(final Book entity)
        {
            return entity.getIsbn();
        }
    };

    public final static BinaryIndexerInteger<Book> POST_ID = new BinaryIndexerInteger.Abstract<>()
    {
        @Override
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

    public static void registerVectorIndex(GigaMap<Book> map)
    {
        VectorIndices<Book> vectorIndices = map.index().register(VectorIndices.Category());

        // Configure the vector index
        VectorIndexConfiguration config = VectorIndexConfiguration.builder()
            .dimension(384)
            .similarityFunction(VectorSimilarityFunction.COSINE)
            .build();

        vectorIndices.add("embeddings", config, new EmbeddingService());
    }
}
