package one.microstream.core.debezium;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.micronaut.eclipsestore.RootProvider;
import one.microstream.domain.microstream.Company;
import org.apache.kafka.connect.runtime.WorkerConfig;
import org.apache.kafka.connect.storage.Converter;
import org.apache.kafka.connect.storage.FileOffsetBackingStore;
import org.apache.kafka.connect.storage.MemoryOffsetBackingStore;
import org.apache.kafka.connect.storage.OffsetUtils;
import org.eclipse.datagrid.cluster.nodelibrary.types.ClusterLockScope;
import org.eclipse.serializer.concurrency.LockedExecutor;
import org.eclipse.store.storage.types.StorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EclipseStoreOffsetBackingStore extends MemoryOffsetBackingStore
{
    private static final Logger log = LoggerFactory.getLogger(FileOffsetBackingStore.class);

    private final Map<String, Set<Map<String, Object>>> connectorPartitions;

    private StorageManager storageManager;
    private RootProvider<Company> rootProvider;
    private LockedExecutor lockedExecutor;

    public EclipseStoreOffsetBackingStore()
    {
        this.connectorPartitions = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void configure(final WorkerConfig config)
    {
        super.configure(config);
        this.storageManager = (StorageManager)config.originals().get("one.microstream.storage.manager");
        this.rootProvider = (RootProvider<Company>)config.originals().get("one.microstream.root.provider");
        this.lockedExecutor = (LockedExecutor)config.originals().get("one.microstream.locked.executor");
    }

    @Override
    public synchronized void start()
    {
        super.start();
        log.info("Starting EclipseStoreOffsetBackingStore");
        this.load();
    }

    @Override
    public synchronized void stop()
    {
        super.stop();
        // Nothing to do since this doesn't maintain any outstanding connections/data
        log.info("Stopped EclipseStoreOffsetBackingStore");
    }

    @SuppressWarnings("unchecked")
    private void load()
    {
        this.lockedExecutor.read(() ->
        {
            final var raw = this.rootProvider.root().debeziumOffsetStore;
            this.data = new HashMap<>();
            for (final var mapEntry : raw.entrySet())
            {
                final var key = (mapEntry.getKey() != null) ? ByteBuffer.wrap(mapEntry.getKey()) : null;
                final var value = (mapEntry.getValue() != null) ? ByteBuffer.wrap(mapEntry.getValue()) : null;
                this.data.put(key, value);
            }
        });
    }

    @Override
    protected void save()
    {
        this.lockedExecutor.write(() ->
        {
            final var raw = new HashMap<byte[], byte[]>();
            for (final var mapEntry : this.data.entrySet())
            {
                final byte[] key = (mapEntry.getKey() != null) ? mapEntry.getKey().array() : null;
                final byte[] value = (mapEntry.getValue() != null) ? mapEntry.getValue().array() : null;
                raw.put(key, value);
            }
            final var root = this.rootProvider.root();
            root.debeziumOffsetStore = raw;
            this.storageManager.store(root);
        });
    }

    @Override
    public Set<Map<String, Object>> connectorPartitions(final String connectorName)
    {
        return this.connectorPartitions.getOrDefault(connectorName, Collections.emptySet());
    }
}
