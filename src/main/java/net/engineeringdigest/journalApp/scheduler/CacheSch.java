package net.engineeringdigest.journalApp.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.engineeringdigest.journalApp.cache.AppCache;

@Component
public class CacheSch {
    

    @Autowired
    private AppCache appCache;

    @Scheduled(cron =  "0 */5 * * * *")
    public void clearCache() {
        appCache.init();
    }
}
