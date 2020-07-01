/*
package com.cs673team1.CourseMaster.course;

import com.cs673team1.CourseMaster.user.User;
import com.cs673team1.CourseMaster.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FavoriteCoursesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockData courseMockData;

    @Autowired
    UserRepository userRepository;

    private final User fakeUser = new User("unique-sso-id-123",
            "vvc",
            "vishnu@tester.com",
            User.Role.ROLE_ADMIN);

    @BeforeEach
    void setUp() {
        userRepository.save(fakeUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.delete(fakeUser);
    }

    @Test
    void retrieveAllFavoriteCoursesTest() throws Exception {
        var principal = buildOAuth2Token("unique-sso-id-123", "vvc").getPrincipal();
        var request = new MockHttpServletRequest();
        request.setUserPrincipal(principal::getName);

        this.mockMvc.perform(get("/api/favoritecourse/me").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


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

}
*/
