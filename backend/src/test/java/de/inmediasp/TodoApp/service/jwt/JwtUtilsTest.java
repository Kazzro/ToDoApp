package de.inmediasp.TodoApp.service.jwt;

import de.inmediasp.TodoApp.service.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

    @Mock
    private UserDetailsImpl userDetails;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtUtils jwtUtils;

    @BeforeEach
    public void setUp() {
        when(userDetails.getUsername()).thenReturn("testuser");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        // when(userDetails.getAuthorities()).thenReturn(Collections.singletonList(() -> "ROLE_USER"));
    }

    @Test
    public void testGenerateJwtToken() {
        jwtUtils.init();
        String token = jwtUtils.generateJwtToken(authentication);
        // Hier kannst du weitergehende Assertions hinzufügen, um sicherzustellen, dass das Token korrekt generiert wurde
        // Zum Beispiel: Überprüfe, ob das Token nicht leer ist
        assertEquals(true, token != null && !token.isEmpty());
    }

}