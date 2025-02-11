package com.test.crud.demo.service;

import com.test.crud.demo.constant.Constant;
import com.test.crud.demo.constant.Constants;
import com.test.crud.demo.dto.LoginRequest;
import com.test.crud.demo.dto.LoginResponse;
import com.test.crud.demo.dto.Response;
import com.test.crud.demo.dto.UserRequest;
import com.test.crud.demo.model.User;
import com.test.crud.demo.repo.UserRepo;
import com.test.crud.demo.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Optional;

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
    @DisplayName("should return success response")
    public void testRegister_Success() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("testuser");
        userRequest.setPassword("testpassword");
        userRequest.setName("Test User");
        userRequest.setRole("USER");

        User user = new User();
        user.setName("Test User");
        user.setUsername("testuser");
        user.setPassword(bCrypt.hashpw(userRequest.getPassword(),BCrypt.gensalt()));
        user.setRole("USER");

        when(userRepo.save(any(User.class))).thenReturn(user);

        Response<Object> response = userServices.register(userRequest);
        assertNotNull(response);
        assertEquals(Constants.Response.SUCCESS_CODE, response.getResponseCode());
        assertEquals(Constants.Response.SUCCESS_MESSAGE, response.getResponseMessage());
        assertNotNull(response.getData());

        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("should return failed response because username is null")
    public void testRegister_Username_Is_Null() {
        UserRequest userRequest = new UserRequest();
        userRequest.setPassword("testpassword");
        userRequest.setName("Test User");
        userRequest.setRole("USER");

        Exception exception = assertThrows(RuntimeException.class, () -> userServices.register(userRequest));
        assertEquals("Invalid Nama Null", exception.getMessage());
    }

    @Test
    @DisplayName("should return failed response because role is null")
    public void testRegister_Role_Is_Null() {
        UserRequest userRequest = new UserRequest();
        userRequest.setPassword("testpassword");
        userRequest.setName("Test User");
        userRequest.setUsername("user");

        Exception exception = assertThrows(RuntimeException.class, () -> userServices.register(userRequest));
        assertEquals("Invalid Role Null", exception.getMessage());
    }

    @Test
    @DisplayName("should return failed response because password is null")
    public void testRegister_Password_Is_Null() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("Test User");
        userRequest.setUsername("user");
        userRequest.setRole("USER");

        Exception exception = assertThrows(RuntimeException.class, () -> userServices.register(userRequest));
        assertEquals("Invalid Password Null", exception.getMessage());
    }


    @Test
    @DisplayName("should return failed response becuase invalid format name")
    public void testRegister_Failure_Invalid_Format_Name() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("testuser");
        userRequest.setPassword("testpassword");
        userRequest.setName("Invalid@Name");
        userRequest.setRole("USER");

        Exception exception = assertThrows(RuntimeException.class, () -> userServices.register(userRequest));
        assertEquals("Invalid Failed Format Nama anda", exception.getMessage());
    }

    @Test
    @DisplayName("should return failed response becuase invalid format role")
    public void testRegister_Failure_Invalid_Format_Role() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("testuser");
        userRequest.setPassword("testpassword");
        userRequest.setName("InvalidName");
        userRequest.setRole("US#R");

        Exception exception = assertThrows(RuntimeException.class, () -> userServices.register(userRequest));
        assertEquals("Invalid Failed Format Role", exception.getMessage());
    }

    @Test
    @DisplayName("Test Login - Success")
    public void testLoginUserFound() {
        String username = "dimasmulya";
        String password = "dimas12345";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User();
        user.setName("Dimas");
        user.setUsername(username);
        user.setPassword(hashedPassword);
        user.setRole("Admin");

        System.out.println("Username dalam test: " + loginRequest.getUsername());

        when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
        System.out.println("Is userRepo a mock? " + Mockito.mockingDetails(userRepo).isMock());

        doReturn(true).when(bCrypt).checkpw(password, user.getPassword());

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(jwtUtils.generateJwtToken(auth)).thenReturn("mock-jwt-token");

        Response<Object> response = userServices.login(loginRequest);

        assertNotNull(response);
        assertEquals(Constants.Response.SUCCESS_CODE, response.getResponseCode());
        assertEquals(Constants.Response.SUCCESS_MESSAGE, response.getResponseMessage());
        assertNotNull(response.getData());
        assertTrue(response.getData() instanceof LoginResponse);
    }




}
