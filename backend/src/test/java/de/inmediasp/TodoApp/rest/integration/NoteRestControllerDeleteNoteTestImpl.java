package de.inmediasp.TodoApp.rest.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.inmediasp.TodoApp.service.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-local.properties")
class NoteRestControllerDeleteNoteTestImpl {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    private final String testUsername1 = "Leonard Nimoy";
    private final String testUsername2 = "Jonathan Frakes";
    private final String testUsername3 = "William Shatner";
    private final String testUsername4 = "Patrick Stewart";

    private String validTestToken;

    public void setupUser(String username) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,"password" ));
        validTestToken = jwtUtils.generateJwtToken(auth);
        System.out.println(jwtUtils.getUserNameFromJwtToken(validTestToken));
    }

    @Test
    void deleteNote() {
    }
}