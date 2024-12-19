package com.test.crud.demo.user;

import com.test.crud.demo.constant.Constants;
import com.test.crud.demo.dto.Response;
import com.test.crud.demo.dto.UserRequest;
import com.test.crud.demo.model.User;
import com.test.crud.demo.repo.UserRepo;
import com.test.crud.demo.security.JwtUtils;
import com.test.crud.demo.service.UserServices;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCrypt;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @InjectMocks
    private UserServices userServices;

    @Mock
    private UserRepo userRepo;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private BCrypt bCrypt;


    @Test
    @Disabled
    public void testRegister_Success() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("testuser");
        userRequest.setPassword("testpassword");
        userRequest.setName("Test User");
        userRequest.setRole("USER");

        User user = new User();
        user.setName("Test User");
        user.setUsername("testuser");
        user.setPassword("hashedpassword");
        user.setRole("USER");
        String password = userRequest.getPassword() != null ? bCrypt.hashpw(userRequest.getPassword(),BCrypt.gensalt()) : "";
        System.out.println(password);
        // Mock behavior of BCrypt
        when(bCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt())).thenReturn("hashedpassword");
        when(userRepo.save(any(User.class))).thenReturn(user);

        // Invoke the register method
        Response<Object> response = userServices.register(userRequest);

        // Assertions
        assertNotNull(response);
        assertEquals(Constants.Response.SUCCESS_CODE, response.getResponseCode());
        assertEquals(Constants.Response.SUCCESS_MESSAGE, response.getResponseMessage());
        assertNotNull(response.getData());

        // Verify interactions
        verify(bCrypt, times(1)).hashpw(password, anyString());
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    public void testRegister_Failure_NullUsername() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(null);
        userRequest.setPassword("testpassword");
        userRequest.setName("Test User");
        userRequest.setRole("USER");

        Exception exception = assertThrows(RuntimeException.class, () -> userServices.register(userRequest));
        assertEquals("Nama tidak boleh kosong", exception.getMessage());
    }

    @Test
    public void testRegister_Failure_InvalidName() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("testuser");
        userRequest.setPassword("testpassword");
        userRequest.setName("Invalid@Name");
        userRequest.setRole("USER");

        Exception exception = assertThrows(RuntimeException.class, () -> userServices.register(userRequest));
        assertEquals("Nama anda tidak valid", exception.getMessage());
    }

    @Test
    public void testRegister_Failure_RepoException() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("testuser");
        userRequest.setPassword("testpassword");
        userRequest.setName("Test User");
        userRequest.setRole("USER");

        when(bCrypt.hashpw(eq("testpassword"), anyString())).thenReturn("hashedpassword");
        when(userRepo.save(any(User.class))).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> userServices.register(userRequest));
        assertEquals("Database error", exception.getMessage());
    }

}
