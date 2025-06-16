package net.engineeringdigest.journalApp.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.engineeringdigest.journalApp.entities.JournalEntry;
import net.engineeringdigest.journalApp.entities.User;
import net.engineeringdigest.journalApp.responseApi.WeatherResponse;
import net.engineeringdigest.journalApp.services.JournalEntryService;
import net.engineeringdigest.journalApp.services.LocationService;
import net.engineeringdigest.journalApp.services.UserEntryService;
import net.engineeringdigest.journalApp.services.WeatherService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserEntryService userEntryService;

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private LocationService locationService;

    @GetMapping
    public ResponseEntity<?> greeting(HttpServletRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String city = locationService.getLocation(request);
        WeatherResponse response = weatherService.getWeather(city);
        String greeting = "";
        if(response!=null) {
            int temp = response.getCurrent().getFeelslike();
            String airQuality = response.getCurrent().getAirQuality().getPm25();
            greeting = "In "+city+",It feels like "+temp+" Calcius and the air quality is "+airQuality+" pm2.5.";
        }
        return new ResponseEntity<>("hi there "+username+"!\n"+greeting,HttpStatus.OK);
   
    }

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
