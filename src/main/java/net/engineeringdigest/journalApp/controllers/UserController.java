package net.engineeringdigest.journalApp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.engineeringdigest.journalApp.entities.JournalEntry;
import net.engineeringdigest.journalApp.entities.User;
import net.engineeringdigest.journalApp.services.JournalEntryService;
import net.engineeringdigest.journalApp.services.UserEntryService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserEntryService userEntryService;

    @Autowired
    private JournalEntryService journalEntryService;

    // @GetMapping("/id/{myid}")
    // public ResponseEntity<?> getUserByID(@PathVariable ObjectId myid) {
    // Optional<User> user = userEntryService.getById(myid);
    // if(user.isPresent()) return new ResponseEntity<>(user,HttpStatus.OK);
    // else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    // }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            userEntryService.saveNewUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User newuser) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userEntryService.findByUserName(username);
        if (user != null) {
            user.setUserName(newuser.getUserName());
            user.setPassword(newuser.getPassword());
            userEntryService.saveNewUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<?> deleteUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!username.isBlank()) {
            User user = userEntryService.findByUserName(username);

            if (user != null) {
                List<JournalEntry> entries = user.getJournalEntries();
                if (entries != null && !entries.isEmpty()) {
                    journalEntryService.deleteAll(entries);
                }
                userEntryService.deleteUser(user);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
