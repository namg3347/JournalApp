package net.engineeringdigest.journalApp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;


import net.engineeringdigest.journalApp.entities.JournalEntry;

public interface JournalEntryRepo extends MongoRepository<JournalEntry,ObjectId>{
    
}
