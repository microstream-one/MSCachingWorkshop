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
    public GigaMap<Book> gigaBooks	= GigaMap.New();

    public Company()
    {
        super();

        BookIndices.registerVectorIndex(gigaBooks);
    }

    public GigaMap<Book> getGigaBooks() {
        return gigaBooks;
    }

    public void setGigaBooks(GigaMap<Book> gigaBooks) {
        this.gigaBooks = gigaBooks;
    }
}
