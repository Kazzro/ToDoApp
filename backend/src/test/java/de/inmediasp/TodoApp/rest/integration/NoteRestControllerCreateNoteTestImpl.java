package de.inmediasp.TodoApp.rest.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.inmediasp.TodoApp.rest.payload.request.NewNoteRequest;
import de.inmediasp.TodoApp.service.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-local.properties")
class NoteRestControllerCreateNoteTestImpl {

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
    void whenUser3PostsNoteRequestWithoutCorrespondent_thenGetNote() throws Exception {
        setupUser(testUsername3);

        NewNoteRequest newNoteRequest = NewNoteRequest.builder().title("valid Title").content("{\"task\": \"valid tasks\", \"status\": false}").build();

        ResultActions response = mockMvc
                .perform(post("/api/user/notes")
                        .header("Authorization","Bearer "+ validTestToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newNoteRequest)))
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.author").value(testUsername3))
                .andExpect(jsonPath("$.correspondent").doesNotExist());
    }

    @Test
    void whenUser4PostsNoteWithCorrespondentUser3_thenUser3GetsReceivedNote() throws Exception {
        setupUser(testUsername3);

        ResultActions preTestResponse = mockMvc
                .perform(get("/api/user/notes/received")
                        .header("Authorization","Bearer "+ validTestToken)
                )
                .andDo(print());

        preTestResponse
                .andDo(print())
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").doesNotExist());


        setupUser(testUsername4);

        NewNoteRequest newNoteRequest = NewNoteRequest.builder().title("valid Title").content("{\"task\": \"valid tasks\", \"status\": false}").correspondent(testUsername3).build();

        ResultActions testResponse = mockMvc
                .perform(post("/api/user/notes")
                        .header("Authorization","Bearer "+ validTestToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newNoteRequest)))
                .andDo(print());

        testResponse
                .andDo(print())
                .andExpect(status()
                        .isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.author").value(testUsername4))
                .andExpect(jsonPath("$.correspondent").value(testUsername3));

        setupUser(testUsername3);

        ResultActions postTestResponse = mockMvc
                .perform(get("/api/user/notes/received")
                        .header("Authorization","Bearer "+ validTestToken)
                )
                .andDo(print());

        postTestResponse
                .andDo(print())
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").doesNotExist());
    }
}