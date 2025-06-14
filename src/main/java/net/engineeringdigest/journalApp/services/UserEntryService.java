package net.engineeringdigest.journalApp.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entities.User;
import net.engineeringdigest.journalApp.repository.UserRepo;

@Service
@Slf4j
public class UserEntryService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //private static final log log = logFactory.getlog(UserEntryService.class);

    public boolean saveNewUser(User user) {
        try {
            log.info("Incoming User... "+user.getUserName());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            log.info("trying to save user...");
            userRepo.save(user);
            log.info("added user"+ user.getUserName());
            return true;
        } catch (Exception e) {
            log.error("username already exists",e);
            return false;
        }

    }

    public void saveAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER", "ADMIN"));
        userRepo.save(user);
    }

    public void saveUser(User user) {
        userRepo.save(user);
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public User findByUserName(String username) {
        return userRepo.findByUserName(username);
    }

    public void deleteUser(User user) {
        userRepo.delete(user);
    }

    public Optional<User> getById(ObjectId id) {
        return userRepo.findById(id);
    }

    public void deleteById(ObjectId id) {
        userRepo.deleteById(id);
    }
}
