package com.cs673team1.CourseMaster.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    private final User fakeUser = new User("unique-sso-id-123",
            "han",
            "han@tester.com",
            User.Role.ROLE_STUDENT);

    @BeforeEach
    void setUp() {
        userRepo.save(fakeUser);
    }

    @AfterEach
    void tearDown() {
        userRepo.delete(fakeUser);
    }

    // simulates authenticated user by creating token normally provided by OAuth2 service
    private static OAuth2AuthenticationToken buildOAuth2Token(String oAuth2Id, String name) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", oAuth2Id);
        attributes.put("email", "han@tester.com");
        attributes.put("name", name);

        List<GrantedAuthority> authorities = Collections.singletonList(
                new OAuth2UserAuthority("ROLE_USER", attributes));
        OAuth2User user = new DefaultOAuth2User(authorities, attributes, "id");
        return new OAuth2AuthenticationToken(user, authorities, "whatever");
    }

    // tests the user in database can be matched against authenticated user
    @Test
    @WithMockUser(username = "Han")
    void userExists() {
        var principal = buildOAuth2Token("unique-sso-id-123", "han").getPrincipal();

        assertTrue(userService.userExists(principal));
    }

    // tests the current authenticated user is returned
    @Test
    void getCurrentUser() {
        // setup fake request with authenticated user oauth2 id
        var principal = buildOAuth2Token("unique-sso-id-123", "han").getPrincipal();
        var request = new MockHttpServletRequest();
        request.setUserPrincipal(principal::getName);

        // compare current user against authenticated user
        var savedUser = userService.getCurrentUser(request);
        assertEquals(savedUser.getoAuth2Id(), request.getUserPrincipal().getName());
    }

    // tests the current authenticated user is returned
    @Test
    void getUserById() {
        var savedUser = userService.getUserById(1);
        assertEquals(1, savedUser.getId());
    }

    // tests that the user is created from principal and saved
    @Test
    void createOAuth2User() {
        // uses different oauth2 id
        var principal = buildOAuth2Token("different-id-456", "han").getPrincipal();
        var createdUser = userService.createOAuth2User(principal);
        assertEquals(createdUser.getoAuth2Id(), "different-id-456");

        // provides blank name
        var anonymous = buildOAuth2Token("different-id-789", "").getPrincipal();
        var createdUser2 = userService.createOAuth2User(anonymous);
        assertEquals(createdUser2.getUserName(), "Anonymous");
    }

    // tests that saved user is updated with details from authenticated user
    @Test
    void updateOAuth2User() {
        // uses same id for authenticated user but new name
        var principal = buildOAuth2Token("unique-sso-id-123", "rob").getPrincipal();
        var updatedUser = userService.updateOAuth2User(principal);
        assertEquals(updatedUser.getUserName(), "rob");

        // attempt to get user by bad oAuth2 id
        var invalidPrincipal = buildOAuth2Token("another-unique-sso-id-999", "rob").getPrincipal();
        var exception = assertThrows(ResponseStatusException.class,
                () -> userService.updateOAuth2User(invalidPrincipal));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }
}