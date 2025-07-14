package net.engineeringdigest.journalApp.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import net.engineeringdigest.journalApp.entities.JournalCacheEntity;
import net.engineeringdigest.journalApp.repository.JournalCacheRepo;

@Component
public class AppCache {
    
    

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
