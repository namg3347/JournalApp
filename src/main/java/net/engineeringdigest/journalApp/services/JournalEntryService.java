package net.engineeringdigest.journalApp.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.engineeringdigest.journalApp.entities.JournalEntry;
import net.engineeringdigest.journalApp.entities.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepo;

@Service // tells @conponentScan that this is a component that can be used by spring ioc
           // to make java bean
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo journalEntryRepo;
    @Autowired
    private UserEntryService userEntryService;

    //@Transactional
    public void saveEntry(JournalEntry journalEntry, String username) {
        try {
            User user = userEntryService.findByUserName(username);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepo.save(journalEntry);
            user.getJournalEntries().add(saved);
            userEntryService.saveUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("something went wrone while saving the entry",e);
        }
    }

    public List<JournalEntry> getAll(String username) {
        User user = userEntryService.findByUserName(username);
        return user.getJournalEntries();
    }

    public Optional<JournalEntry> getById(ObjectId id) {
        return journalEntryRepo.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String userName) {
        boolean removed = false;
        try {
            User user = userEntryService.findByUserName(userName);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
        if(removed) {
            userEntryService.saveUser(user);
            journalEntryRepo.deleteById(id);
        }
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("error error occured while deleting the entry.");
        }
        return removed;    
    }

    public void deleteAll(List<JournalEntry> entires) {
        journalEntryRepo.deleteAll(entires);
    }


    public void saveEntry( JournalEntry entry) {
        journalEntryRepo.save(entry);
    }


}
