package de.inmediasp.TodoApp.rest;

import de.inmediasp.TodoApp.rest.payload.request.LoginRequest;
import de.inmediasp.TodoApp.rest.payload.request.SignupRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthController {
    ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest);
    ResponseEntity<?> registerUser(@RequestBody @Valid SignupRequest signupRequest);
}
