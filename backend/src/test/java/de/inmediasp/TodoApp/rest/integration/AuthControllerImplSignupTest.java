package de.inmediasp.TodoApp.rest.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.inmediasp.TodoApp.rest.payload.request.SignupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-local.properties")
public class AuthControllerImplSignupTest {


    //@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenSignupValid_thenReturnMessageCreated() throws Exception {
        SignupRequest signupRequest = SignupRequest.builder().username("validName").password("validPassword").email("validEmail@test.test").build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andDo(print());

        response.andDo(print()).andExpect(status().isCreated());
    }

    @Test
    public void whenSignupInvalidEmail_thenReturnMessageError() throws Exception {
        SignupRequest signupRequest = SignupRequest.builder().username("validName").password("validPassword").email("invalid").build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andDo(print());

        response.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void whenSignupNullEmail_thenReturnMessageError() throws Exception {
        SignupRequest signupRequest = SignupRequest.builder().username("validName").password("validPassword").build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andDo(print());

        response.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void whenSignupInvalidPassword_thenReturnMessageError() throws Exception {
        SignupRequest signupRequest = SignupRequest.builder().username("validName").password("short").email("validEmail@test.test").build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andDo(print());

        response.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void whenSignupNullPassword_thenReturnMessageError() throws Exception {
        SignupRequest signupRequest = SignupRequest.builder().username("validName").email("validEmail@test.test").build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andDo(print());

        response.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void whenSignupInvalidUsername_thenReturnMessageError() throws Exception {
        SignupRequest signupRequest = SignupRequest.builder().username("no").password("validPassword").email("validEmail@test.test").build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andDo(print());

        response.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void whenSignupNullUsername_thenReturnMessageError() throws Exception {
        SignupRequest signupRequest = SignupRequest.builder().password("validPassword").email("validEmail@test.test").build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andDo(print());

        response.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void whenSignupNull_thenReturnMessageError() throws Exception {
        SignupRequest signupRequest = null;

        ResultActions response = mockMvc
                .perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andDo(print());

        response.andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void whenSignupBuildNullReferences_thenReturnMessageError() throws Exception {
        SignupRequest signupRequest = SignupRequest.builder().build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andDo(print());

        response.andDo(print()).andExpect(status().isBadRequest());
    }



}
