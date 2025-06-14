package net.engineeringdigest.journalApp.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTest {
    
    @Autowired
    private EmailService emailService;

    @Test
    public void emailTest() {
        String to = "ng72942@gmail.com";
        String subject = "I can hear voices a year in future...";
        String text = "Today the voices stopped.";
        emailService.sendMail(to,subject,text);
    }
}
