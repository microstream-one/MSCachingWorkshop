package one.microstream.core.debezium;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

@Singleton
public class DebeziumConfig
{
    private static final Logger LOG = LoggerFactory.getLogger(DebeziumConfig.class);

    @Value("${datasources.default.url}")
    private String jdbcUrl;

    @Value("${datasources.default.username}")
    private String username;

    @Value("${datasources.default.password}")
    private String password;

    public Properties buildProperties()
    {
        ensureDirectoryExists();

        final Properties props = new Properties();

        props.setProperty("name", "engine");

        // Connector configuration
        props.setProperty("connector.class", "io.debezium.connector.postgresql.PostgresConnector");
        props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
        props.setProperty("offset.storage.file.filename", "debezium/offsets.dat");
        props.setProperty("offset.flush.interval.ms", "1000");

        // Database connection from existing properties
        final JdbcUrlParts urlParts = parseJdbcUrl(this.jdbcUrl);
        props.setProperty("database.hostname", urlParts.host());
        props.setProperty("database.port", String.valueOf(urlParts.port()));
        props.setProperty("database.user", this.username);
        props.setProperty("database.password", this.password);
        props.setProperty("database.dbname", urlParts.dbName());

        // Table filtering
        props.setProperty("table.include.list", "public.books");

        // Topic and naming
        props.setProperty("topic.prefix", "caching-workshop");

        // PostgreSQL specific
        props.setProperty("plugin.name", "pgoutput");
        props.setProperty("slot.name", "debezium_books_slot");

        // Schema history
        props.setProperty("schema.history.internal", "io.debezium.storage.file.history.FileSchemaHistory");
        props.setProperty("schema.history.internal.file.filename", "debezium/schema-history.dat");

        return props;
    }

    private void ensureDirectoryExists()
    {
        try
        {
            Files.createDirectories(Path.of("debezium"));
        }
        catch (final IOException e)
        {
            throw new RuntimeException("Could not create debezium directory", e);
        }
    }

    private JdbcUrlParts parseJdbcUrl(final String url)
    {
        // jdbc:postgresql://host:port/dbname
        final String stripped = url.replace("jdbc:postgresql://", "");
        final String[] hostPortAndDb = stripped.split("/", 2);
        final String[] hostAndPort = hostPortAndDb[0].split(":");
        return new JdbcUrlParts(
                hostAndPort[0],
                hostAndPort.length > 1 ? Integer.parseInt(hostAndPort[1]) : 5432,
                hostPortAndDb.length > 1 ? hostPortAndDb[1] : ""
        );
    }

    private record JdbcUrlParts(String host, int port, String dbName) {}
}
