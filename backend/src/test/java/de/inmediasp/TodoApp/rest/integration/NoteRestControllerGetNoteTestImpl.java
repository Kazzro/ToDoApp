package de.inmediasp.TodoApp.rest.integration;

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
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-local.properties")
class NoteRestControllerGetNoteTestImpl {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;
    private final String testUsername1 = "Leonard Nimoy";
    private final String testUsername2 = "Jonathan Frakes";
    private final String testUsername3 = "William Shatner";
    private final String testUsername4 = "Patrick Stewart";

    private String validTestToken;



    @Test
    void whenUser1CallsNotes_thenGetAllNotesConnectedToUser1() throws Exception {
        setupUser(testUsername1);

        ResultActions response = mockMvc
                .perform(get("/api/user/notes")
                        .header("Authorization","Bearer "+ validTestToken)
                )
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value("e0db994f-6cfb-4f76-9c1f-2d9261563bed"))
                .andExpect(jsonPath("$[1].id").value("d4d1cfc0-f9e6-4088-b78b-26825c9fcdc4"))
                .andExpect(jsonPath("$[2].id").value("7e7446b8-6d3d-4da2-b7a0-4f676d3db7a0"))
                ;
    }

    @Test
    void whenUser2CallsNotes_thenGetAllNotesConnectedToUser2() throws Exception {
        setupUser(testUsername2);

        ResultActions response = mockMvc
                .perform(get("/api/user/notes")
                        .header("Authorization","Bearer "+ validTestToken)
                )
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value("7e7446b8-6d3d-4da2-b7a0-4f676d3db7a0"))
                .andExpect(jsonPath("$[1].id").value("d4d1cfc0-f9e6-4088-b78b-26825c9fcdc4"))
                .andExpect(jsonPath("$[2].id").value("b0a76d0f-b62f-41ff-b9e2-8b62f19fb9e6"))
                .andExpect(jsonPath("$[3].id").value("b8aae362-0e8e-4d86-8406-e3620e8efbe7"))
                ;
    }

    @Test
    void whenNullCallsNotes_thenGetUnauthorized() throws Exception {

        ResultActions response = mockMvc
                .perform(get("/api/user/notes")
                )
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isUnauthorized());
    }

    @Test
    void whenFakeHeaderCallsNotes_thenGetUnauthorized() throws Exception {

        ResultActions response = mockMvc
                .perform(get("/api/user/notes")
                        .header("Authorization","Bearer "))
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isUnauthorized());
    }

    @Test
    void whenUser1CallsReceived_thenGetAllReceivedNotesForUser1() throws Exception {
        setupUser(testUsername1);

        ResultActions response = mockMvc
                .perform(get("/api/user/notes/received")
                        .header("Authorization","Bearer "+ validTestToken)
                        )
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].note_ID").value("d4d1cfc0-f9e6-4088-b78b-26825c9fcdc4"))
                .andExpect(jsonPath("$[0].target.username").value(testUsername1))
                .andExpect(jsonPath("$[1].note_ID").doesNotExist());
    }

    @Test
    void whenUser2CallsReceived_thenGetAllReceivedNotesForUser2() throws Exception {
        setupUser(testUsername2);

        ResultActions response = mockMvc
                .perform(get("/api/user/notes/received")
                        .header("Authorization","Bearer "+ validTestToken)
                )
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].note_ID").value("7e7446b8-6d3d-4da2-b7a0-4f676d3db7a0"))
                .andExpect(jsonPath("$[0].target.username").value(testUsername2))
                ;
    }


    @Test
    void whenNullCallsReceived_thenGetUnauthorized() throws Exception {

        ResultActions response = mockMvc
                .perform(get("/api/user/notes/received")
                )
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isUnauthorized());
    }

    @Test
    void whenFakeHeaderCallsReceived_thenGetUnauthorized() throws Exception {

        ResultActions response = mockMvc
                .perform(get("/api/user/notes/received")
                        .header("Authorization","Bearer "))
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isUnauthorized());
    }

    @Test
    void whenUser1CallsPersonal_thenGetAllPersonalNotesCreatedByUser1() throws Exception {
        setupUser(testUsername1);

        ResultActions response = mockMvc
                .perform(get("/api/user/notes/personal")
                        .header("Authorization","Bearer "+ validTestToken)
                )
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].note_ID").value("e0db994f-6cfb-4f76-9c1f-2d9261563bed"))
                .andExpect(jsonPath("$[0].author.username").value(testUsername1))
                .andExpect(jsonPath("$[1].note_ID").value("7e7446b8-6d3d-4da2-b7a0-4f676d3db7a0"))
                .andExpect(jsonPath("$[1].author.username").value(testUsername1))
                ;
    }

    @Test
    void whenUser2CallsPersonal_thenGetAllNotesCreatedByUser2() throws Exception {
        setupUser(testUsername2);

        ResultActions response = mockMvc
                .perform(get("/api/user/notes/personal")
                        .header("Authorization","Bearer "+ validTestToken)
                )
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].note_ID").value("d4d1cfc0-f9e6-4088-b78b-26825c9fcdc4"))
                .andExpect(jsonPath("$[0].author.username").value(testUsername2))
                .andExpect(jsonPath("$[1].note_ID").value("b0a76d0f-b62f-41ff-b9e2-8b62f19fb9e6"))
                .andExpect(jsonPath("$[1].author.username").value(testUsername2))
                .andExpect(jsonPath("$[2].note_ID").value("b8aae362-0e8e-4d86-8406-e3620e8efbe7"))
                .andExpect(jsonPath("$[2].author.username").value(testUsername2))
                .andExpect(jsonPath("$[3]").doesNotExist());
    }

    @Test
    void whenNullCallsPersonal_thenGetUnauthorized() throws Exception {

        ResultActions response = mockMvc
                .perform(get("/api/user/notes/personal")
                )
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isUnauthorized());
    }

    @Test
    void whenFakeHeaderCallsPersonal_thenGetUnauthorized() throws Exception {

        ResultActions response = mockMvc
                .perform(get("/api/user/notes/personal")
                        .header("Authorization","Bearer "))
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isUnauthorized());
    }

    public void setupUser(String username) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,"password" ));
        validTestToken = jwtUtils.generateJwtToken(auth);
        System.out.println(jwtUtils.getUserNameFromJwtToken(validTestToken));
    }
}