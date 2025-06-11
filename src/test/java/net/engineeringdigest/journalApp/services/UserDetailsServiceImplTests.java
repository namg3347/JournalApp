package net.engineeringdigest.journalApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import net.engineeringdigest.journalApp.entities.User;
import net.engineeringdigest.journalApp.repository.UserRepo;

@SpringBootTest
public class UserDetailsServiceImplTests {
    
    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    
    private UserRepo userRepo;

    @Test
    @Disabled
    public void loadUserByUsernameTest() {
        when(userRepo.findByUserName(ArgumentMatchers.anyString())).thenReturn(
            User.builder().userName("ram").password("sfasf")
            .roles(new ArrayList<String>()).build());
        UserDetails user = userDetailsServiceImpl.loadUserByUsername("ram");
        assertNotNull(user);
    }
}
