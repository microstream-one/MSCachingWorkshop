package one.microstream.core.debezium;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.debezium.engine.ChangeEvent;
import io.micronaut.eclipsestore.RootProvider;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import one.microstream.dao.microstream.DAOBook;
import one.microstream.domain.microstream.Book;
import one.microstream.domain.microstream.DataRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DebeziumChangeHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(DebeziumChangeHandler.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    RootProvider<DataRoot> rootProvider;

    @Inject
    DAOBook daoBook;

    //private int count = 0;
    //private final Thread debounceThread = new Thread(this::runDebouncer, "DebeziumDebouncer");
    //private volatile boolean shuttingDown = false;

    public DebeziumChangeHandler()
    {
        //this.debounceThread.start();
    }

    //private void runDebouncer()
    //{
    //    while(!shuttingDown)
    //    {
    //
    //    }
    //}

    public void handleChangeEvent(final ChangeEvent<String, String> event)
    {
        final String value = event.value();
        if (value == null)
        {
            return;
        }

        try
        {
            final JsonNode root = this.objectMapper.readTree(value);
            final JsonNode payload = root.has("payload") ? root.get("payload") : root;
            final String op = payload.get("op").asText();

            switch (op)
            {
            case "c": // INSERT
            case "r": // READ (snapshot)
            {
                final Book book = this.mapToBook(payload.get("after"));
                this.daoBook.insert(book);
                //// NOTE: Instead of inserting each one we store after every 500th entry to combat write spam
                //this.rootProvider.root().gigaBooks.add(book);
                //LOG.info("Debezium INSERT/READ: Book with postId={} added to cache", book.getPostId());
                break;
            }
            case "u": // UPDATE
            {
                final Book book = this.mapToBook(payload.get("after"));
                this.daoBook.update(book);
                LOG.info("Debezium UPDATE: Book with postId={} updated in cache", book.getPostId());
                break;
            }
            case "d": // DELETE
            {
                final JsonNode before = payload.get("before");
                final int postId = before.get("id").asInt();
                this.daoBook.deleteByPostId(postId);
                LOG.info("Debezium DELETE: Book with postId={} removed from cache", postId);
                break;
            }
            default:
                LOG.warn("Unknown Debezium operation: {}", op);
            }
        }
        catch (final Exception e)
        {
            LOG.error("Error processing Debezium change event", e);
        }

        //count++;
        //
        //if (count % 1000 == 0)
        //{
        //    System.out.println("Storing...");
        //    this.rootProvider.root().gigaBooks.store();
        //    LazyReferenceManager.get().cleanUp();
        //    System.gc();
        //    count = 0;
        //}
    }

    private Book mapToBook(final JsonNode node)
    {
        final Book book = new Book();
        book.setPostId(node.get("id").asInt());
        book.setTitle(node.has("title") && !node.get("title").isNull() ? node.get("title").asText() : null);
        book.setAuthor(node.has("author") && !node.get("author").isNull() ? node.get("author").asText() : null);
        book.setGenre(node.has("genre") && !node.get("genre").isNull() ? node.get("genre").asText() : null);
        book.setIsbn(node.has("isbn") && !node.get("isbn").isNull() ? node.get("isbn").asText() : null);
        book.setPages(node.has("pages") && !node.get("pages").isNull() ? node.get("pages").asInt() : 0);
        book.setPublisher(
            node.has("publisher") && !node.get("publisher").isNull() ? node.get("publisher").asText() : null);
        book.setDescription(
            node.has("description") && !node.get("description").isNull() ? node.get("description").asText() : null);
        book.setLanguage(node.has("language") && !node.get("language").isNull() ? node.get("language").asText() : null);
        book.setYear(node.has("year") && !node.get("year").isNull() ? node.get("year").asInt() : 0);
        book.setPrice(node.has("price") && !node.get("price").isNull() ? node.get("price").asDouble() : 0);
        return book;
    }

    //@PreDestroy
    //public void destroy() throws InterruptedException
    //{
    //    this.shuttingDown = true;
    //
    //    final boolean joinSuccess = this.debounceThread.join(Duration.ofSeconds(10));
    //    if (!joinSuccess)
    //    {
    //        LOG.error("Failed to wait for debounce thread to stop");
    //    }
    //}
}
