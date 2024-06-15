package de.inmediasp.TodoApp.rest;

import de.inmediasp.TodoApp.rest.payload.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.UUID;

public interface UserController {
    ResponseEntity<String> getMyUsername();
    ResponseEntity<List<UserResponse>> getAllUsers();
    ResponseEntity<List<String>> getUsernameList();
    ResponseEntity<UserResponse> getUsernameByID(@PathVariable UUID userID, @RequestHeader HttpServletRequest request);
}
