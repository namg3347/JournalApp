package net.engineeringdigest.journalApp.entities;


import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.engineeringdigest.journalApp.enums.Sentiment;

@Document(collection = "Journal_Entries")//Mongodb feature to mark this call as document  
@Data//Lombok feature that makes getters,setters,toString,hashcode,equals method during compile time
@NoArgsConstructor
public class JournalEntry {
    
    @Id//unique identifier for mongodb
    private ObjectId id;

    private String title;

    private String body;

    private LocalDateTime date;

    private Sentiment sentiment;

}
