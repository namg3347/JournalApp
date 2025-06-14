package net.engineeringdigest.journalApp.entities;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Users")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {

    @Id
    private ObjectId id;
    @Indexed(unique = true)//uses userName as indes making searching easy,it is not automatically indexed we have to change the property to make auto-indexing possible
    @NonNull
    private String userName;
    @NonNull
    private String password;
    private String email;
    private boolean sentimentAnalysis;
    @DBRef//creates a reference of journal entries into users ie their id used to refrence the entries into a perticular user
    private List<JournalEntry> journalEntries = new ArrayList<>();
    private List<String> roles;



}
