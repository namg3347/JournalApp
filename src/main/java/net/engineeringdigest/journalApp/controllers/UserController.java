package net.engineeringdigest.journalApp.controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entities.JournalEntry;
import net.engineeringdigest.journalApp.entities.User;
import net.engineeringdigest.journalApp.responseApi.WeatherResponse;
import net.engineeringdigest.journalApp.services.JournalEntryService;
import net.engineeringdigest.journalApp.services.LocationService;
import net.engineeringdigest.journalApp.services.UserEntryService;
import net.engineeringdigest.journalApp.services.WeatherService;
import net.engineeringdigest.journalApp.utils.JwtUtil;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserEntryService userEntryService;

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${jwt.expiration}")
    private long jwtexpiration;

    @Autowired
    private JwtUtil jwtUtil;

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

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        try {
            userEntryService.saveNewUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(@RequestBody User user,HttpServletRequest request,HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
            Cookie cookie = new Cookie("jwt", jwtUtil.generateToken(user.getUserName()));
            cookie.setHttpOnly(true);
            cookie.setSecure(true); 
            cookie.setPath("/");
            cookie.setMaxAge((int) (jwtexpiration / 1000));
            response.addCookie(cookie);
            return new ResponseEntity<>("login succesful",HttpStatus.OK);
        } catch (Exception e) {
            log.error("Couldn't generate authorised jwt token", e);
            return new ResponseEntity<>("Incorrect Username or Password",HttpStatus.BAD_REQUEST);
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
