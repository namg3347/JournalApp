package net.engineeringdigest.journalApp.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import net.engineeringdigest.journalApp.entities.User;

@Component
public class UserRepoImpl {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUserForSA() {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[A-Z|a-z]{2,6}$"));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));

        return mongoTemplate.find(query, User.class);
    }
 
}
