package one.microstream.core.init;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.data.connection.annotation.Connectable;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

@Singleton
public class MicronautStartup implements ApplicationEventListener<ServerStartupEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(MicronautStartup.class);

    @Inject
    DataSource dataSource;
    @Inject
    @Named("pg-listener")
    ExecutorService executorService;

    private Connection connection;
    private volatile boolean running = true;

    @Override
    @Connectable
    public void onApplicationEvent(ServerStartupEvent event) {
        try {
            initializeListener();
        } catch (SQLException e) {
            throw new RuntimeException("Error starting PostgreSQL listener", e);
        }

    }

    @Override
    public boolean supports(ServerStartupEvent event) {
        return ApplicationEventListener.super.supports(event);
    }

    private void initializeListener() throws SQLException {
        this.connection = dataSource.getConnection();
        PGConnection pgConnection = connection.unwrap(PGConnection.class);

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("LISTEN data_changed");
        }
        LOG.info("LISTEN to channel 'data_changed'");

        executorService.submit(() -> pollNotifications(pgConnection));
    }

    private void pollNotifications(PGConnection pgConnection) {
        while (running) {
            try {
                Optional.ofNullable(pgConnection.getNotifications(5000))
                        .stream()
                        .flatMap(Arrays::stream)
                        .forEach(this::handleNotification);
            } catch (SQLException e) {
                LOG.error("Error retrieving notifications", e);
            }
        }
    }

    private void handleNotification(PGNotification notification) {
        String channel = notification.getName();
        String payload = notification.getParameter();

        LOG.info("Received notification on channel {}: {}", channel, payload);

        // Process your notification here
    }

    @Connectable
    @PreDestroy
    public void stop() {
        running = false;
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                LOG.info("PostgreSQL listener connection closed");
            }
        } catch (SQLException e) {
            LOG.warn("Error closing PostgreSQL connection", e);
        }
    }

}
