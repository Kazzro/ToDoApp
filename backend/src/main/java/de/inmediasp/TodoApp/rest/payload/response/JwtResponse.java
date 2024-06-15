package de.inmediasp.TodoApp.rest.payload.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;


@Data
public class JwtResponse {
    private String accessToken;
    private String type = "Bearer";
    private UUID id;
    private String username;
    private String email;
    private List<String> roles;

    public JwtResponse(String token, UUID id, String username, String email, List<String> roles) {
        this.accessToken = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
