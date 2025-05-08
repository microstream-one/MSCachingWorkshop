package one.microstream.core.init;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.micronaut.context.annotation.Value;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.data.connection.annotation.Connectable;
import io.micronaut.runtime.event.ApplicationStartupEvent;
import io.micronaut.runtime.server.event.ServerShutdownEvent;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import one.microstream.dao.microstream.DAOBook;
import one.microstream.dao.microstream.postgres.PostDAOBook;
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
public class InitPostgresBooksNotifier implements ApplicationEventListener<Object> {

    private static final Logger LOG = LoggerFactory.getLogger(InitPostgresBooksNotifier.class);

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

    @Override
    @Connectable
    public void onApplicationEvent(final Object event)
    {
    	 if (event instanceof ApplicationStartupEvent)
         {
             try {
                 this.initializeListener();
             } catch (final SQLException e) {
                 throw new RuntimeException("Error starting PostgreSQL listener", e);
             }
         }
         else if (event instanceof ServerShutdownEvent)
         {
             this.shutdown();
         }

    }

    private void initializeListener() throws SQLException
    {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(1);
        config.setPoolName("ListenerPool");

        HikariDataSource rawDataSource = new HikariDataSource(config);

        this.connection = rawDataSource.getConnection();
        final PGConnection pgConnection = this.connection.unwrap(PGConnection.class);

        try (Statement stmt = this.connection.createStatement()) {
            stmt.execute("LISTEN data_changed");
        }
        LOG.info("LISTEN to channel 'data_changed'");

        this.executorService.submit(() -> this.pollNotifications(pgConnection));
    }

    private void pollNotifications(final PGConnection pgConnection) {
        while (this.running) {
            try {
                Optional.ofNullable(pgConnection.getNotifications(5000))
                        .stream()
                        .flatMap(Arrays::stream)
                        .forEach(this::handleNotification);
            } catch (final SQLException e) {
                LOG.error("Error retrieving notifications", e);
            }
        }
    }

    private void handleNotification(final PGNotification notification)
    {
        final String channel = notification.getName();
        final String payload = notification.getParameter();

        daoBook.insert(notification);
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
