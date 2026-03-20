package one.microstream.core.debezium;

import io.micronaut.eclipsestore.RootProvider;
import one.microstream.domain.microstream.DataRoot;
import org.eclipse.serializer.concurrency.LockedExecutor;
import org.eclipse.store.storage.types.StorageManager;

public final class Instances
{
    private static volatile StorageManager storageManager;
    private static volatile LockedExecutor lockedExecutor;
    private static volatile RootProvider<DataRoot> rootProvider;

    public static synchronized StorageManager storageManager()
    {
        return storageManager;
    }

    public static synchronized void storageManager(final StorageManager storageManager)
    {
        Instances.storageManager = storageManager;
    }

    public static synchronized LockedExecutor lockedExecutor()
    {
        return lockedExecutor;
    }

    public static synchronized void lockedExecutor(final LockedExecutor lockedExecutor)
    {
        Instances.lockedExecutor = lockedExecutor;
    }

    public static synchronized RootProvider<DataRoot> rootProvider()
    {
        return rootProvider;
    }

    public static synchronized void rootProvider(final RootProvider<DataRoot> rootProvider)
    {
        Instances.rootProvider = rootProvider;
    }

    private Instances()
    {
    }
}
