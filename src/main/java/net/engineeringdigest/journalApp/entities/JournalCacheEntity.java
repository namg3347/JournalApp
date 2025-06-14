package net.engineeringdigest.journalApp.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "JournalCacheConfig")//Mongodb feature to mark this call as document  
@Data//Lombok feature that makes getters,setters,toString,hashcode,equals method during compile time
@NoArgsConstructor
public class JournalCacheEntity {

    private String key;

    private String value;
    

}