package one.microstream.core.init;

import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import one.microstream.dao.microstream.DAOBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class InitDbSynchronizer
{
    @Inject
    DAOBook daoBook;

    private static final Logger LOG = LoggerFactory.getLogger(InitDbSynchronizer.class);
    @Scheduled(fixedRate = "60s")
    public void executeTask()
    {
        LOG.info("Synchronizing database is running.");
        daoBook.performStartupSync();
        LOG.info("Successfully synchronized database.");
    }
}
