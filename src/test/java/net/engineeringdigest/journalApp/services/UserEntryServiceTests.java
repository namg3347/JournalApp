package net.engineeringdigest.journalApp.services;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import net.engineeringdigest.journalApp.entities.User;
import net.engineeringdigest.journalApp.repository.UserRepo;

@SpringBootTest
// @ActiveProfiles("dev")
public class UserEntryServiceTests {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserEntryService userEntryService;

    //JAVA COVERANCE LEFT...........

    
    @Test
    @Disabled
    public void testFindUserByUsername() {
        assertNotNull(userRepo.findByUserName("ram"));
    }

    @ParameterizedTest
    @Disabled
    @ValueSource(strings = {
        "ram",
        "shayam",
        "naman"
    })
    public void testFindUserByUsername2(String username) {
        assertNotNull(userRepo.findByUserName(username),"this is false "+username);
    }

    @ParameterizedTest
    @Disabled
    @ArgumentsSource(UserArgumentProvider.class)
    public void testSaveNewUser(User user) {
        // assertNotNull(user);
        assertTrue(userEntryService.saveNewUser(user));
    }

    
    @ParameterizedTest
    @Disabled
    @CsvSource({
        "1,1,2",
        "2,3,5",
        "1,6,3",
        "34,63,12"
    })
    public void adder(int a,int b,int expected) {
        assertEquals(expected, a+b);
    }


    // @AfterAll - method run after all other test
    // @BeforeAll - method run before all other test
    // @AfterEach - runs after each
    // @BeforeEach - runs before each
    public void setup() {

    }
}
