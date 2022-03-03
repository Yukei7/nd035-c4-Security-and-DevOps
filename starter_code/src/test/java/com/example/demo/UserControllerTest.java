package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private UserController userController;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);

        User user1 = TestUtils.createUser(1L);

        when(bCryptPasswordEncoder.encode("testpw1")).thenReturn("hashed1");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findByUsername("user1")).thenReturn(user1);
    }

    @Test
    public void createUserSuccess() {
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("test1");
        req.setPassword("testpw1");
        req.setConfirmPassword("testpw1");

        final ResponseEntity<User> response = userController.createUser(req);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test1", user.getUsername());
        assertEquals("hashed1", user.getPassword());
    }

    @Test
    public void findUserByIdSuccess() {
        final ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals("user1", user.getUsername());
    }

    @Test
    public void findUserByNameSuccess() {
        final ResponseEntity<User> response = userController.findByUserName("user1");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals("user1", user.getUsername());
    }

    @Test
    public void createUserInconsistentPassword() {
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("test1");
        req.setPassword("testpw1");
        req.setConfirmPassword("testpw2");
        final ResponseEntity<User> response = userController.createUser(req);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void createUserShortPassword() {
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("test1");
        req.setPassword("11");
        req.setConfirmPassword("11");
        final ResponseEntity<User> response = userController.createUser(req);
        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void findUserByIdNotFound() {
        final ResponseEntity<User> response = userController.findById(4121L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void findUserByNameNotFound() {
        final ResponseEntity<User> response = userController.findByUserName("user123");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}
