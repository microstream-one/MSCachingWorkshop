package one.microstream.core.debezium;

import java.io.IOException;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.eclipsestore.RootProvider;
import io.micronaut.runtime.event.ApplicationStartupEvent;
import io.micronaut.runtime.server.event.ServerShutdownEvent;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import one.microstream.domain.microstream.Company;
import org.eclipse.datagrid.cluster.nodelibrary.types.ClusterFoundation;
import org.eclipse.datagrid.cluster.nodelibrary.types.ClusterLockScope;
import org.eclipse.datagrid.cluster.nodelibrary.types.StorageNodeManager;
import org.eclipse.store.storage.types.StorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DebeziumBookListener implements ApplicationEventListener<Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(DebeziumBookListener.class);

    private final boolean isProdMode;
    private final StorageNodeManager clusterNodeManager;

    private DebeziumEngine<ChangeEvent<String, String>> engine;

    @Inject
    DebeziumConfig debeziumConfig;
    @Inject
    DebeziumChangeHandler changeHandler;

    @Inject
    RootProvider<Company> rootProvider;
    @Inject
    StorageManager storageManager;
    @Inject
    ClusterLockScope lockScope;

    public DebeziumBookListener(final ClusterFoundation<?> clusterFoundation)
    {
        final var props = clusterFoundation.getNodelibraryPropertiesProvider();
        this.isProdMode = props.isProdMode();
        this.clusterNodeManager = this.isProdMode && !props.isBackupNode()
                                  ? clusterFoundation.getStorageNodeManager()
                                  : null;
    }

    @Override
    public void onApplicationEvent(final Object event)
    {
        if (event instanceof ApplicationStartupEvent)
        {
            if (this.isProdMode)
            {
                if (this.clusterNodeManager == null)
                {
                    return;
                }
                if (!this.clusterNodeManager.isDistributor())
                {
                    return;
                }
            }
            this.startEngine();
        }
        else if (event instanceof ServerShutdownEvent)
        {
            this.stopEngine();
        }
    }

    private void startEngine()
    {
        this.engine = DebeziumEngine.create(Json.class)
            .using(this.debeziumConfig.buildProperties(this.storageManager, this.rootProvider, this.lockScope))
            .notifying(this.changeHandler::handleChangeEvent)
            .build();

        final Thread engineThread = new Thread(this.engine, "debezium-engine");
        engineThread.setDaemon(true);
        engineThread.start();

        LOG.info("Debezium CDC engine started for table 'public.books'");
    }

    @PreDestroy
    public void stopEngine()
    {
        if (this.engine != null)
        {
            try
            {
                this.engine.close();
                LOG.info("Debezium CDC engine stopped");
            }
            catch (final IOException e)
            {
                LOG.warn("Error stopping Debezium engine", e);
            }
        }
    }
}
