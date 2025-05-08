package one.microstream.core.init;

import io.micronaut.context.event.ApplicationEventListener;
import jakarta.inject.Inject;
import one.microstream.dao.microstream.DAOBook;

public class InitDBStartupTasks implements ApplicationEventListener<Object>
{
    @Inject
    DAOBook daoBook;

    @Override
    public void onApplicationEvent(Object event) {
        daoBook.performStartupSync();
    }
}
