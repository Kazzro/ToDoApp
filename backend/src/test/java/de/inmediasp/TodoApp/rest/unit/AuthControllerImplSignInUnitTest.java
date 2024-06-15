package de.inmediasp.TodoApp.rest.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.UUID;

import de.inmediasp.TodoApp.dao.UserRepository;
import de.inmediasp.TodoApp.rest.AuthControllerImpl;
import de.inmediasp.TodoApp.rest.payload.request.LoginRequest;
import de.inmediasp.TodoApp.service.UserDetailsImpl;
import de.inmediasp.TodoApp.service.jwt.JwtUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(
        locations = "classpath:application-local.properties")
class AuthControllerImplSignInUnitTest {

    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private AuthControllerImpl controller;

    @Test
    void authenticateUser_ValidCredentials_ReturnsJwtResponse() {
        // Arrange
        LoginRequest loginRequest = LoginRequest.builder().username("username").password("password").build();
        UserDetailsImpl userDetails = new UserDetailsImpl(new UUID(1L,1L), "username", "email", "password", Collections.emptyList());


        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("mockJwtToken");


        // Act
        ResponseEntity<?> response = controller.authenticateUser(loginRequest);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

    }

    @Test
    void authenticateUser_ValidCredentials_ReturnsJwtResponse2() {
        // Arrange
        LoginRequest loginRequest = LoginRequest.builder().username("username").password("password").build();
        UserDetailsImpl userDetails = new UserDetailsImpl(new UUID(1L,1L), "username", "email", "password", Collections.emptyList());


        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication))
                .thenReturn("mockJwtToken");

        // Act
        ResponseEntity<?> response = controller.authenticateUser(loginRequest);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

    }


}