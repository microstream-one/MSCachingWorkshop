package one.microstream.domain.microstream;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import one.microstream.core.ollama.EmbeddingService;
import one.microstream.domain.indices.BookIndices;
import org.eclipse.store.gigamap.jvector.VectorIndex;
import org.eclipse.store.gigamap.jvector.VectorIndexConfiguration;
import org.eclipse.store.gigamap.jvector.VectorIndices;
import org.eclipse.store.gigamap.jvector.VectorSimilarityFunction;
import org.eclipse.store.gigamap.types.BitmapIndices;
import org.eclipse.store.gigamap.types.GigaMap;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class Company
{
    EmbeddingService embeddingService;

    public GigaMap<Book> gigaBooks	= GigaMap.New();
    VectorIndices<Book> vectorIndices = gigaBooks.index().register(VectorIndices.Category());
    // Configure the vector index
    VectorIndexConfiguration config = VectorIndexConfiguration.builder()
            .dimension(768)
            .similarityFunction(VectorSimilarityFunction.COSINE)
            .build();

    VectorIndex<Book> index = vectorIndices.add("embeddings", config, embeddingService);

    public Company(EmbeddingService embeddingService)
    {
        super();
        this.embeddingService = embeddingService;
    }

    public GigaMap<Book> getGigaBooks() {
        return gigaBooks;
    }

    public void setGigaBooks(GigaMap<Book> gigaBooks) {
        this.gigaBooks = gigaBooks;
    }
}
