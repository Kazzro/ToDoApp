package de.inmediasp.TodoApp.rest;

import de.inmediasp.TodoApp.dao.RoleRepository;
import de.inmediasp.TodoApp.dao.UserRepository;
import de.inmediasp.TodoApp.entity.Role;
import de.inmediasp.TodoApp.entity.RoleEnums;
import de.inmediasp.TodoApp.entity.User;
import de.inmediasp.TodoApp.rest.payload.request.LoginRequest;
import de.inmediasp.TodoApp.rest.payload.request.SignupRequest;
import de.inmediasp.TodoApp.rest.payload.response.JwtResponse;
import de.inmediasp.TodoApp.rest.payload.response.MessageResponse;
import de.inmediasp.TodoApp.service.UserDetailsImpl;
import de.inmediasp.TodoApp.service.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthControllerImpl implements AuthController{
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername()))
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        if (userRepository.existsByEmail(signupRequest.getEmail()))
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));

        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleEnums.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(RoleEnums.ROLE_MANAGER).orElseThrow(() -> new RuntimeException("Error: Role not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role moderatorRole = roleRepository.findByName(RoleEnums.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: Role not found."));
                        roles.add(moderatorRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(RoleEnums.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return new ResponseEntity<>(new MessageResponse("User registered successfully!"), HttpStatus.CREATED);
    }
}
