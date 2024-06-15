package de.inmediasp.TodoApp.rest.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.inmediasp.TodoApp.rest.payload.request.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-local.properties")
class AuthControllerImplLoginTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenLoginValid_thenReturnOk() throws Exception{
        LoginRequest loginRequest = LoginRequest.builder().username("Leonard Nimoy").password("password").build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isOk());
    }
    @Test
    public void whenLoginValid_thenDoesNotReturnPassword() throws Exception{
        LoginRequest loginRequest = LoginRequest.builder().username("Leonard Nimoy").password("password").build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$.password").doesNotExist());
    }
    @Test
    public void whenLoginValid_thenReturnJwtToken() throws Exception{
        LoginRequest loginRequest = LoginRequest.builder().username("Leonard Nimoy").password("password").build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$.type", is("Bearer"))
                        .hasJsonPath())
                .andExpect(jsonPath("$.accessToken").exists());
    }
    @Test
    public void whenLoginValid_thenReturnUsernameAndEmail() throws Exception{
        LoginRequest loginRequest = LoginRequest.builder().username("Leonard Nimoy").password("password").build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$.username", is(loginRequest.getUsername()))
                        .hasJsonPath())
                .andExpect(jsonPath("$.email", is("test@test.test"))
                        .hasJsonPath());
    }

    @Test
    public void whenLoginInvalid_thenReturnUnauthorized() throws Exception{
        LoginRequest loginRequest = LoginRequest.builder().username("noSuchUserInDb").password("wrongPassword").build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isUnauthorized());
    }

    @Test
    public void whenLoginInvalidUser_thenReturnUnauthorized() throws Exception{
        LoginRequest loginRequest = LoginRequest.builder().username("noSuchUserInDb").password("password").build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isUnauthorized());
    }

    @Test
    public void whenLoginInvalidPasswordForExistingUser_thenReturnUnauthorized() throws Exception{
        LoginRequest loginRequest = LoginRequest.builder().username("Leonard Nimoy").password("wrongPassword").build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isUnauthorized());
    }

    @Test
    public void whenLoginPasswordShort_thenReturnBadRequest() throws Exception{
        LoginRequest loginRequest = LoginRequest.builder().username("Leonard Nimoy").password("short").build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    public void whenLoginPasswordEmpty_thenReturnBadRequest() throws Exception{
        LoginRequest loginRequest = LoginRequest.builder().username("Leonard Nimoy").password("").build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    public void whenLoginPasswordNull_thenReturnBadRequest() throws Exception{
        LoginRequest loginRequest = LoginRequest.builder().username("Leonard Nimoy").build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    public void whenLoginUsernameTooShort_thenReturnBadRequest() throws Exception{
        LoginRequest loginRequest = LoginRequest.builder().username("a").password("password").build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    public void whenLoginUsernameEmpty_thenReturnBadRequest() throws Exception{
        LoginRequest loginRequest = LoginRequest.builder().username("").password("password").build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    public void whenLoginUsernameNull_thenReturnBadRequest() throws Exception{
        LoginRequest loginRequest = LoginRequest.builder().password("password").build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    public void whenLoginUsernameAndPasswordNull_thenReturnBadRequest() throws Exception{
        LoginRequest loginRequest = LoginRequest.builder().build();

        ResultActions response = mockMvc
                .perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isBadRequest());
    }

    @Test
    public void whenLoginIsNull_thenReturnBadRequest() throws Exception{
        LoginRequest loginRequest = null;

        ResultActions response = mockMvc
                .perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print());

        response
                .andDo(print())
                .andExpect(status()
                        .isBadRequest());
    }
}