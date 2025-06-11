package net.engineeringdigest.journalApp.controllers;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.engineeringdigest.journalApp.entities.JournalEntry;
import net.engineeringdigest.journalApp.entities.User;
import net.engineeringdigest.journalApp.services.JournalEntryService;
import net.engineeringdigest.journalApp.services.UserEntryService;

@RestController // RestController = ResponseBody(ensures that the return value of each method is
                // automatically serialized to JSON or XML and sent back in the HTTP response
                // body,)
// + Controller(tells Spring that this class is a web controller)
@RequestMapping("/journal") // maps the restcontroller to a keyword or url
public class JournalControllerV2 {

    @Autowired // (field/dependency injection)Spring iot creats a takes the java bean and gives
               // a instance of the class ie. objects for that class
    private JournalEntryService journalEntryService;
    @Autowired
    private UserEntryService userEntryService;

    @GetMapping // maps get request to a url
    public ResponseEntity<?> getAllJournalEntriesOfUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<JournalEntry> all = journalEntryService.getAll(username);
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping // maps post request to url
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry entry) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            journalEntryService.saveEntry(entry, username);
            return new ResponseEntity<JournalEntry>(entry, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<JournalEntry>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myid}") // maps get request but with a path variable that insures unique requests
    public ResponseEntity<?> getById(@PathVariable ObjectId myid) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userEntryService.findByUserName(username);
        if (user != null) {
            Optional<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myid))
                    .findFirst();
            if (collect.isPresent()) {
                Optional<JournalEntry> entry = journalEntryService.getById(myid);
                if (entry.isPresent()) {
                    return new ResponseEntity<>(entry.get(), HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/id/{myid}")
    public ResponseEntity<?> putById(@PathVariable ObjectId myid, @RequestBody JournalEntry newEntry) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userEntryService.findByUserName(username);
        if (user != null) {
            Optional<JournalEntry> old = user.getJournalEntries().stream().filter(x -> x.getId().equals(myid))
                    .findFirst();
            if (old.isPresent()) {
                if (old.isPresent()) {
                    old.get().setTitle(
                            newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle()
                                    : old.get().getTitle());
                    old.get().setBody(newEntry.getBody() != null && !newEntry.getBody().equals("") ? newEntry.getBody()
                            : old.get().getBody());
                    journalEntryService.saveEntry(old.get());
                    return new ResponseEntity<>(old.get(), HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("id/{myid}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId myid) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean removed = journalEntryService.deleteById(myid, username);
        if (removed)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
