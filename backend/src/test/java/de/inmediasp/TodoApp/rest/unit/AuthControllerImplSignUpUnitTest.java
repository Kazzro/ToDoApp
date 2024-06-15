package de.inmediasp.TodoApp.rest.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.inmediasp.TodoApp.dao.RoleRepository;
import de.inmediasp.TodoApp.dao.UserRepository;
import de.inmediasp.TodoApp.entity.Role;
import de.inmediasp.TodoApp.entity.RoleEnums;
import de.inmediasp.TodoApp.entity.User;
import de.inmediasp.TodoApp.rest.payload.request.SignupRequest;
import de.inmediasp.TodoApp.service.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(
        locations = "classpath:application-local.properties")
public class AuthControllerImplSignUpUnitTest {
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;
    @Autowired
    private MockMvc mockMvc;


    @Test
    void signupUser_validSignup_returnsSuccessAndUserRole() throws Exception {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        setupRole(RoleEnums.ROLE_USER);
        SignupRequest signupRequest = SignupRequest.builder().username("username").email("email@example.com").password("password").build();

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(signupRequest))).andExpect(status().isCreated()).andExpect(jsonPath("$.message").value("User registered successfully!"));

        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
    void signupUser_inValidSignupUsername_returnsMessage() throws Exception {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        SignupRequest signupRequest = SignupRequest.builder().username("username").email("email@example.com").password("password").build();

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(signupRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("Error: Username is already taken!"));

        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void signupUser_inValidSignupEmail_returnsMessage() throws Exception {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        SignupRequest signupRequest = SignupRequest.builder().username("username").email("email@example.com").password("password").build();

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(signupRequest))).andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("Error: Email is already taken!"));

        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void signUpUser_NullRoles() throws Exception {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        Set<Role> expectedRoles = setupRole(RoleEnums.ROLE_USER);

        SignupRequest signupRequest = SignupRequest.builder().username("username").email("email@example.com").password("password").build();

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signupRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        // Verifying that the UserRepository's save method was called with the correct arguments
        verify(userRepository, times(1)).save(argThat(argument ->
                argument.getUsername().equals(signupRequest.getUsername()) &&
                        argument.getEmail().equals(signupRequest.getEmail())));

        verify(userRepository, times(1)).save(argThat(userWithExpectedRoles(expectedRoles)));

    }

    @Test
    public void signUpUser_adminRole() throws Exception {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);

        Set<Role> expectedRoles = setupRole(RoleEnums.ROLE_MANAGER);

        SignupRequest signupRequest = SignupRequest.builder().username("username").email("email@example.com").password("password").roles(Collections.singleton("admin")).build();

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signupRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        // Verifying that the UserRepository's save method was called with the correct arguments
        verify(userRepository, times(1)).save(argThat(userWithExpectedRoles(expectedRoles)));
    }

    @Test
    public void signUpUser_managerRole() throws Exception {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        Set<Role> expectedRoles = setupRole(RoleEnums.ROLE_MODERATOR);

        SignupRequest signupRequest = SignupRequest.builder().username("username").email("email@example.com").password("password").roles(Collections.singleton("mod")).build();

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(signupRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        // Verifying that the UserRepository's save method was called with the correct arguments
        verify(userRepository, times(1)).save(argThat(userWithExpectedRoles(expectedRoles)));
    }


    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Set<Role> setupRole(RoleEnums... roles){
        Set<Role> expectedRoles = new HashSet<>();
        Arrays.stream(roles).forEach(r -> {
            Role mockedRole = new Role(r);
            expectedRoles.add(mockedRole);
            when(roleRepository.findByName(r)).thenReturn(Optional.of(mockedRole));
        });

        return expectedRoles;
    }

    private ArgumentMatcher<User> userWithExpectedRoles(Set<Role> expectedRoles) {
        return argument -> {
            Set<Role> actualRoles = argument.getRoles();
            return actualRoles.containsAll(expectedRoles) && expectedRoles.containsAll(actualRoles);
        };
    }
}
