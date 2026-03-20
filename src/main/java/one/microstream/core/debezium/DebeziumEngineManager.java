package one.microstream.core.debezium;

import java.io.IOException;
import java.time.Duration;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import one.microstream.domain.microstream.DataRoot;
import org.eclipse.datagrid.cluster.nodelibrary.types.ClusterFoundation;
import org.eclipse.datagrid.cluster.nodelibrary.types.StorageNodeManager;
import org.eclipse.serializer.concurrency.LockedExecutor;
import org.eclipse.store.storage.types.StorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DebeziumEngineManager
{
    private static final Logger LOG = LoggerFactory.getLogger(DebeziumEngineManager.class);

    private final StorageNodeManager clusterNodeManager;
    private final boolean isProd;

    private volatile boolean shuttingDown = false;

    private DebeziumEngine<ChangeEvent<String, String>> engine;
    private Thread debeziumThread;

    @Inject
    DebeziumConfig debeziumConfig;
    @Inject
    DebeziumChangeHandler changeHandler;

    @Inject
    RootProvider<DataRoot> rootProvider;
    @Inject
    StorageManager storageManager;
    @Inject
    LockedExecutor lockedExecutor;

    public DebeziumEngineManager(final ClusterFoundation<?> clusterFoundation)
    {
        final var props = clusterFoundation.getNodelibraryPropertiesProvider();
        this.isProd = props.isProdMode();
        this.clusterNodeManager = this.isProd && !props.isBackupNode()
                                  ? clusterFoundation.getStorageNodeManager()
                                  : null;
    }

    @Scheduled(fixedDelay = "5s")
    public void checkForDistributor()
    {
        if (!this.shuttingDown && this.engine == null && (!this.isProd
            || this.clusterNodeManager != null && this.clusterNodeManager.isDistributor()))
        {
            this.startEngine();
        }
    }

    private void startEngine()
    {
        LOG.info("Starting Debezium engine");

        this.engine = DebeziumEngine.create(Json.class)
            .using(this.debeziumConfig.buildProperties(this.storageManager, this.rootProvider, this.lockedExecutor))
            .notifying(this.changeHandler::handleChangeEvent)
            .build();

        System.out.print("DEBEZIUM ROOT SIZE: ");
        System.out.println(this.rootProvider.root().getDebeziumOffsetStore().size());
        for(final var entry : this.rootProvider.root().getDebeziumOffsetStore().entrySet())
        {
            System.out.println("Entry key size: " + entry.getKey().length + ", value size: " + entry.getValue().length);
        }

        this.debeziumThread = new Thread(this.engine, "debezium-engine");
        this.debeziumThread.start();
    }

    @PreDestroy
    public void stopEngine() throws InterruptedException
    {
        this.shuttingDown = true;

        if (this.engine == null)
        {
            return;
        }

        LOG.info("Stopping Debezium Engine");

        try
        {
            this.engine.close();
        }
        catch (final IOException e)
        {
            LOG.error("Failed to close Debezium", e);
        }

        final boolean joinSuccess = this.debeziumThread.join(Duration.ofSeconds(10));
        if (!joinSuccess)
        {
            LOG.error("Failed to wait for Debezium thread to stop");
        }

        this.engine = null;
        this.debeziumThread = null;
    }
}
