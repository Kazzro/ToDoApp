package de.inmediasp.TodoApp.rest;

import de.inmediasp.TodoApp.rest.payload.response.UserResponse;
import de.inmediasp.TodoApp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserControllerImpl implements UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/my/username")
    @PreAuthorize("isAuthenticated")
    public ResponseEntity<String> getMyUsername(){
        return new ResponseEntity<>(userService.getMyUsername(), HttpStatus.OK);
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/username")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<String>> getUsernameList() {
        return new ResponseEntity<>(userService.getAllUserNames(),HttpStatus.OK);
    }

    @GetMapping("/username/{userID}")
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<UserResponse> getUsernameByID(@PathVariable UUID userID, @RequestHeader HttpServletRequest request) {
        return new ResponseEntity<>(userService.findById(userID), HttpStatus.OK);
    }

}