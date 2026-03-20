package one.microstream.core.ollama;

import java.time.Duration;
import java.util.List;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import one.microstream.domain.microstream.Book;
import org.eclipse.store.gigamap.jvector.Vectorizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmbeddingService extends Vectorizer<Book>
{
    private static final Logger LOG = LoggerFactory.getLogger(EmbeddingService.class);
    private static final String MODEL_URL = "http://localhost:11434";
    private static final String MODEL_NAME = "all-minilm:l6-v2";

    private transient OllamaEmbeddingModel model;

    private OllamaEmbeddingModel model()
    {
        if (this.model == null)
        {
            this.model = OllamaEmbeddingModel.builder()
                .baseUrl(MODEL_URL)
                .modelName(MODEL_NAME)
                .timeout(Duration.ofSeconds(120))
                .logRequests(false)
                .logResponses(false)
                .build();
        }
        return this.model;
    }

    @Override
    public List<float[]> vectorizeAll(final List<? extends Book> entities)
    {
        final var texts = entities.stream().map(b -> TextSegment.from(this.toEmbeddingText(b))).toList();
        return this.model().embedAll(texts).content().stream().map(Embedding::vector).toList();
    }

    @Override
    public float[] vectorize(final Book b)
    {
        return this.vectorize(this.toEmbeddingText(b));
    }

    public float[] vectorize(final String text)
    {
        return this.model().embed(text).content().vector();
    }

    public String toEmbeddingText(final Book b)
    {
        return String.format("Title: %s", b.getTitle());
    }
}
