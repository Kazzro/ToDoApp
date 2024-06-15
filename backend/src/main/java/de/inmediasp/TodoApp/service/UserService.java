package de.inmediasp.TodoApp.service;

import de.inmediasp.TodoApp.dao.UserRepository;
import de.inmediasp.TodoApp.entity.User;

import de.inmediasp.TodoApp.rest.payload.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String getMyUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(UserResponse::new).collect(Collectors.toList());
    }

    public List<String> getAllUserNames() {
        return userRepository.findAll().stream().map(User::getUsername).toList();
    }

    public UserResponse findById(UUID theId) {
        Optional<User> result = userRepository.findById(theId);
        User theUser;

        if (result.isPresent()) {
            theUser = result.get();
        } else {
            throw new RuntimeException("no User with provided UUID present " + theId);
        }

        return new UserResponse(theUser);
    }

    public User findByUsername(String username) {
        Optional<User> result = userRepository.findByUsername(username);
        User theUser;

        if (result.isPresent()) {
            theUser = result.get();
        } else {
            throw new RuntimeException("no User with provided username present " + username);
        }


        return theUser;
    }


    public User save(User theEntity) {
        return userRepository.save(theEntity);
    }


    public void deleteById(UUID theId) {
        userRepository.deleteById(theId);
    }
}
