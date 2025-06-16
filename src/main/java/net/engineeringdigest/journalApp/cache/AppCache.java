package net.engineeringdigest.journalApp.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.engineeringdigest.journalApp.entities.JournalCacheEntity;
import net.engineeringdigest.journalApp.repository.JournalCacheRepo;

@Component
public class AppCache {
    
    public enum keys{
        WEATHER_API,
        LOCATION_API;
    }

    public Map<String,String> appCache;

    @Autowired
    private JournalCacheRepo journalCacheRepo;


    @PostConstruct
    public void init() {
        appCache = new HashMap<>(); 
        List<JournalCacheEntity> all = journalCacheRepo.findAll();
        for(JournalCacheEntity entity:all) {
            appCache.put(entity.getKey(), entity.getValue());
        }
    }
}
