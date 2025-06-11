package net.engineeringdigest.journalApp.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import net.engineeringdigest.journalApp.entities.User;

public interface UserRepo extends MongoRepository<User,ObjectId>{

    User findByUserName(String username);

}
