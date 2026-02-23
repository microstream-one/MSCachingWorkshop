package one.microstream.core.init;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Value;
import io.micronaut.data.connection.annotation.Connectable;
import io.vertx.core.Vertx;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.pubsub.PgSubscriber;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import one.microstream.dao.microstream.DAOBook;
import one.microstream.domain.microstream.Book;
import org.eclipse.datagrid.cluster.nodelibrary.types.ClusterFoundation;
import org.eclipse.datagrid.cluster.nodelibrary.types.StorageNodeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;

@Context
public class InitPostgresBooksNotifier {

    private static final Logger LOG = LoggerFactory.getLogger(InitPostgresBooksNotifier.class);

    private final boolean isProdMode;
    @Inject
    private final StorageNodeManager clusterNodeManager;
    @Inject
    private final Vertx vertx;
    @Inject
    private final PgConnectOptions connectOptions;
    @Inject
    private final ObjectMapper objectMapper;

    public InitPostgresBooksNotifier(final ClusterFoundation<?> clusterFoundation, Vertx vertx,
                                     PgConnectOptions connectOptions, ObjectMapper objectMapper)
    {
        final var props = clusterFoundation.getNodelibraryPropertiesProvider();
        this.isProdMode = props.isProdMode();
        this.clusterNodeManager = this.isProdMode && !props.isBackupNode()
                ? clusterFoundation.getStorageNodeManager()
                : null;
        this.vertx = vertx;
        this.connectOptions = connectOptions;
        this.objectMapper = objectMapper;
    }

    @Value("${datasources.default.url}")
    private String jdbcUrl;

    @Value("${datasources.default.username}")
    private String username;

    @Value("${datasources.default.password}")
    private String password;

    @Inject
    DAOBook daoBook;
    @Inject
    @Named("pg-listener")
    ExecutorService executorService;

    private Connection connection;
    private volatile boolean running = true;

    @PostConstruct
    public void init() {
        // Der PgSubscriber ist in 5.0.7 das Mittel der Wahl für LISTEN/NOTIFY
        PgSubscriber subscriber = PgSubscriber.subscriber(vertx, connectOptions);

        // Verbindung aufbauen
        subscriber.connect().onComplete(ar -> {
            if (ar.succeeded()) {
                LOG.info("Erfolgreich mit PostgreSQL für NOTIFY verbunden.");

                // Auf den Kanal 'data_changed' horchen
                subscriber.channel("data_changed").handler(payload -> {
                    LOG.info("Event empfangen: {}", payload);
                    processPayload(payload);
                });

            } else {
                LOG.error("Verbindung für LISTEN fehlgeschlagen: ", ar.cause());
            }
        });

        // Optional: Reconnect-Strategie
        subscriber.reconnectPolicy(retries -> {
            LOG.warn("Verbindung verloren. Versuch {} für Reconnect...", retries);
            return 1000L * Math.min(retries, 30); // Max 30 Sekunden Pause
        });
    }

    private void processPayload(String payload) {
        try {
            // Wir nutzen TypeReference, um das generische T als 'Book' zu definieren
            Book book = objectMapper.readValue(payload, Book.class);

            LOG.info("📖 Book Event: {} - ID: {}, Title: {}", book.getIsbn(), book.getTitle());


        } catch (Exception e) {
            LOG.error("Fehler beim Mapping des Payloads auf Klasse Book", e);
        }
    }
    
    @PreDestroy
    public void stop() {
        this.shutdown();
    }

    @Connectable
    public void shutdown() {
        this.running = false;
        try {
            if ((this.connection != null) && !this.connection.isClosed()) {
                this.connection.close();
                LOG.info("PostgreSQL listener connection closed");
            }
        } catch (final SQLException e) {
            LOG.warn("Error closing PostgreSQL connection", e);
        }
    }

}
