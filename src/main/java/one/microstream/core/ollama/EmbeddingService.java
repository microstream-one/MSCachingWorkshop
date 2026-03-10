package one.microstream.core.ollama;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.output.Response;
import jakarta.inject.Singleton;
import one.microstream.controller.RBBookFilter;
import one.microstream.domain.microstream.Book;
import org.eclipse.store.gigamap.jvector.Vectorizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class EmbeddingService extends Vectorizer<Book>
{
    private static final Logger LOG = LoggerFactory.getLogger(EmbeddingService.class);
    OllamaEmbeddingModel model = null;
    String ollamaURL = "http://localhost:11434";
    String modelName = "all-minilm:l6-v2";

//    public void embeddAllBooks(List<Book> books, int batchSize) {
//        int totalBooks = books.size();
//        LOG.info("Starte Embedding-Prozess für {} Bücher in {}-er Batches", totalBooks, batchSize);
//
//        // Wir nutzen IntStream, um elegant durch die Indizes zu springen
//        IntStream.range(0, (totalBooks + batchSize - 1) / batchSize)
//                .map(i -> i * batchSize)
//                .forEach(start -> {
//                    int end = Math.min(start + batchSize, totalBooks);
//                    List<Book> batch = books.subList(start, end);
//
//                    processBatch(batch);
//
//                    LOG.info("Fortschritt: {}/{} Bücher verarbeitet", end, totalBooks);
//                });
//    }

//    private void processBatch(List<Book> batch) {
//        // Texte für Ollama vorbereiten
//        List<TextSegment> texts = batch.stream().map(b -> TextSegment.from(toEmbeddingText(b)))
//                .collect(Collectors.toList());
//
//        try {
//            Response<List<Embedding>> response = model.embedAll(texts);
//            List<Embedding> embeddings = response.content();
//
//            // Vektoren zurück in die Buch-Objekte schreiben
//            for (int i = 0; i < batch.size(); i++) {
//                float[] vector = embeddings.get(i).vector();
//                batch.get(i).setEmbeddings(vector);
//            }
//        } catch (Exception e) {
//            LOG.error("Fehler beim Verarbeiten des Batches: {}", e.getMessage());
//        }
//    }

    @Override
    public List<float[]> vectorizeAll(List<? extends Book> entities)
    {
        if (model == null)
        {
            this.model = OllamaEmbeddingModel.builder()
                    .baseUrl(ollamaURL)
                    .modelName(modelName) // Ein sehr gutes Modell für Embeddings
                    .timeout(Duration.ofSeconds(120)) // Mehr Puffer für Docker-Starts
                    .logRequests(true)               // Hilfreich für das Debugging
                    .logResponses(true)
                    .build();
        }

        // Texte für Ollama vorbereiten
        List<TextSegment> texts = entities.stream().map(b -> TextSegment.from(toEmbeddingText(b)))
                .collect(Collectors.toList());

        Response<List<Embedding>> response = model.embedAll(texts);

        List<float[]> collect = response.content().stream().map(e -> e.vector())
                .collect(Collectors.toUnmodifiableList());

        return collect;
    }


    @Override
    public float[] vectorize(Book b) {
        if (model == null)
        {
            this.model = OllamaEmbeddingModel.builder()
                    .baseUrl(ollamaURL)
                    .modelName(modelName) // Ein sehr gutes Modell für Embeddings
                    .timeout(Duration.ofSeconds(120)) // Mehr Puffer für Docker-Starts
                    .logRequests(true)               // Hilfreich für das Debugging
                    .logResponses(true)
                    .build();
        }

        // 2. Embedding generieren
        System.out.println("Generiere Embedding...");
        Response<Embedding> response = model.embed(toEmbeddingText(b));

        // 3. Ergebnis ausgeben
        float[] vector = response.content().vector();

        return  vector;
    }

    public float[] vectorize(String text) {
        if (model == null)
        {
            this.model = OllamaEmbeddingModel.builder()
                    .baseUrl(ollamaURL)
                    .modelName(modelName) // Ein sehr gutes Modell für Embeddings
                    .timeout(Duration.ofSeconds(120)) // Mehr Puffer für Docker-Starts
                    .logRequests(true)               // Hilfreich für das Debugging
                    .logResponses(true)
                    .build();
        }

        // 2. Embedding generieren
        System.out.println("Generiere Embedding...");
        Response<Embedding> response = model.embed(text);

        // 3. Ergebnis ausgeben
        float[] vector = response.content().vector();
        System.out.println("Dimension des Vektors: " + vector.length);
        System.out.println("Erste 5 Werte: " +
                vector[0] + ", " + vector[1] + ", " + vector[2] + ", " + vector[3] + ", " + vector[4]);

        return  vector;
    }

    public String toEmbeddingText(final Book b) {
        return String.format("search_document: %s", b.getTitle());
    }


}
