package net.engineeringdigest.journalApp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import net.engineeringdigest.journalApp.entities.JournalCacheEntity;

public interface JournalCacheRepo extends MongoRepository<JournalCacheEntity,ObjectId>{
    
}
