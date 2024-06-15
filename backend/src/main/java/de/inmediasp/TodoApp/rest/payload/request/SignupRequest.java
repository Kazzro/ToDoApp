package de.inmediasp.TodoApp.rest.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class SignupRequest {
    @Size(min = 3, message = "Username must be at least 3 characters long")
    @NotBlank(message = "Username is mandatory")
    private String username;
    @Email(message = "Please provide a valid email address")
    private String email;
    private Set<String> roles;
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @NotBlank(message = "Password is mandatory")
    private String password;
}
