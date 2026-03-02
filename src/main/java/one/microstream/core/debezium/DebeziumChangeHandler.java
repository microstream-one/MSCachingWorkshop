package one.microstream.core.debezium;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.debezium.engine.ChangeEvent;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import one.microstream.dao.microstream.DAOBook;
import one.microstream.domain.microstream.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DebeziumChangeHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(DebeziumChangeHandler.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    DAOBook daoBook;

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
                    LOG.debug("Debezium INSERT/READ: Book with postId={} added to cache", book.getPostId());
                    break;
                }
                case "u": // UPDATE
                {
                    final Book book = this.mapToBook(payload.get("after"));
                    this.daoBook.update(book);
                    LOG.debug("Debezium UPDATE: Book with postId={} updated in cache", book.getPostId());
                    break;
                }
                case "d": // DELETE
                {
                    final JsonNode before = payload.get("before");
                    final int postId = before.get("id").asInt();
                    this.daoBook.deleteByPostId(postId);
                    LOG.debug("Debezium DELETE: Book with postId={} removed from cache", postId);
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
        return book;
    }
}
