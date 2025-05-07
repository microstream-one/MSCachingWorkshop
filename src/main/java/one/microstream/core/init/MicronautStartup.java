package one.microstream.core.init;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import jakarta.inject.Inject;
import one.microstream.dao.microstream.DAOBook;

public class MicronautStartup implements ApplicationEventListener<ServerStartupEvent> {

    @Inject
    DAOBook daoBook;

    @Override
    public void onApplicationEvent(ServerStartupEvent event) {
//        if(baseData.isPresent())
//        {
//            final AppBaseData appBaseData = baseData.get();
//            System.out.println("Database already initialized");
//        }
//        else
//        {
//            System.out.println("No initial data found");
//            dataInitializer.initData();
//
//            final AppBaseData appBaseData = new AppBaseData();
//            appBaseData.setFirstStart(false);
//            appBaseData.setAppName("Sustainability HUB");
//            daoAppBaseData.insert(appBaseData);
//        }
    }

    @Override
    public boolean supports(ServerStartupEvent event) {
        return ApplicationEventListener.super.supports(event);
    }
}
