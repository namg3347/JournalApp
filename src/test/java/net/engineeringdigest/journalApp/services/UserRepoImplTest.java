package net.engineeringdigest.journalApp.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.engineeringdigest.journalApp.repository.UserRepoImpl;

@SpringBootTest
public class UserRepoImplTest {
    
    @Autowired
    private UserRepoImpl userRepoImpl;

    @Test
    public void queryTest() {
        assertNotNull(userRepoImpl.getUserForSA());
    }
}
