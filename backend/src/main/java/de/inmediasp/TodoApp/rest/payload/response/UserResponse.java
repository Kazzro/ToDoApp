package de.inmediasp.TodoApp.rest.payload.response;

import de.inmediasp.TodoApp.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private String username;
    private String email;

    public UserResponse(User theUser){
        this.username = theUser.getUsername();
        this.email = theUser.getEmail();
    }
}
