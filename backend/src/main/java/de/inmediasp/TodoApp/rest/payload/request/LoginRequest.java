package de.inmediasp.TodoApp.rest.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginRequest {
    @Size(min = 3, message = "Username must be at least 3 characters long")
    @NotBlank(message = "Username is mandatory")
    private String username;
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @NotBlank(message = "Password is mandatory")
    private String password;
}
