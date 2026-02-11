package one.microstream.core.init;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Inject;
import one.microstream.dao.microstream.DAOBook;

public class InitDBStartupTasks implements ApplicationEventListener<StartupEvent>
{
    @Inject
    DAOBook daoBook;

    @Override
    public void onApplicationEvent(StartupEvent event) {
        daoBook.performStartupSync();
    }
}
