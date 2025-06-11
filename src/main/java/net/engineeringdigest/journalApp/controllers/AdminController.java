package net.engineeringdigest.journalApp.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.engineeringdigest.journalApp.entities.User;
import net.engineeringdigest.journalApp.services.UserEntryService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserEntryService userEntryService;
    
    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        List<User> list = userEntryService.getAll();
        if(list!=null  && !list.isEmpty()) {
            return new ResponseEntity<>(list,HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody User user ) {
        try {
            userEntryService.saveAdmin(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/convert-to-admin/{username}")
    public ResponseEntity<?> convertUserToAdmin(@RequestBody String username) {
        try {
            User user = userEntryService.findByUserName(username);
            user.setRoles(Arrays.asList("USER","ADMIN"));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
