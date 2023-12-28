package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @Mock
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    @Mock
    private final CartRepository cartRepository = Mockito.mock(CartRepository.class);

    private final PasswordEncoder passwordEncoder = Mockito.mock(BCryptPasswordEncoder.class);

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    public static User createUser(String username, String password){
        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    @Test
    public void createUser_200() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("password123");

        ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
    }

    @Test
    public void findById_200() {
        User user = createUser("username1", "password123");

        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void findById_NotFound_404() {
        ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void findByUserName_200() {
        String username = "username1";
        User user = createUser(username, "password123");

        Mockito.when(userRepository.findByUsername(username))
                        .thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName(username);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void findByUserName_NotFound_404() {
        String username = "username1";
        ResponseEntity<User> response = userController.findByUserName(username);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
