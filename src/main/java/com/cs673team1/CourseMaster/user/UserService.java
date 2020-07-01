package com.cs673team1.CourseMaster.user;

import com.cs673team1.CourseMaster.course.Course;
import com.cs673team1.CourseMaster.review.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


@Service
public class UserService {

    /**
     * Creates Logger object to be use by this class.
     */
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * Creates UserService object to be use by this class.
     */
    @Autowired
    private UserRepository userRepo;

    /**
     * Checks if authenticated user exists.
     *
     * @param principal OAuth2User object for obtaining AuthenticatedPrincipal
     * @return Returns if user exists or not
     */
    public boolean userExists(OAuth2User principal) {
        String oAuth2Id = principal.getName();
        return userRepo.findByOAuth2Id(oAuth2Id) != null;
    }

    /**
     * Returns the current signed in user.
     *
     * @param req HttpServletRequest object to obtain the signed in User
     * @return Returns User using oAuth2ID for query
     */
    public User getCurrentUser(HttpServletRequest req) {
        var principal = req.getUserPrincipal();
        String oAuth2Id = principal.getName();
        return userRepo.findByOAuth2Id(oAuth2Id);
    }

    /**
     *
     * @param userId unique id of user.
     * @return user entity with nested reviews.
     */
    public User getUserById(Integer userId) {
        Optional<User> savedUser = userRepo.findById(userId);
        if (savedUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Requested user id not found");
        }
        return savedUser.get();
    }

    /**
     * Creates an account for a new user with OAuth2 details.
     *
     * @param principal OAuth2User object for obtaining AuthenticatedPrincipal
     * @return Returns User name or Anonymous if no name is available
     */
    public User createOAuth2User(OAuth2User principal) {
        // extracts details from authenticated user
        var userAttr = principal.getAttributes();
        String oAuth2Id = principal.getName();
        String name = (String) userAttr.get("name");
        String email = (String) userAttr.get("email");
        if (name == null || name.isBlank()) { // github users may not have name
            name = "Anonymous";
        }

        // TODO: extract dynamic role from Principal (selected in frontend)
        var role = User.Role.ROLE_STUDENT;

        var user = new User(oAuth2Id, name, email, role);
        var savedUser = userRepo.save(user);
        logger.info("User created: " + user.toString());
        return savedUser;
    }

    /**
     * Updates/Returns an existing user.
     *
     * @param principal OAuth2User object for obtaining AuthenticatedPrincipal
     * @return Returns User
     */
    public User updateOAuth2User(OAuth2User principal) {
        var userAttr = principal.getAttributes();
        var user = userRepo.findByOAuth2Id(principal.getName());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "user does not exist");
        }
        user.setUserName((String) userAttr.get("name"));
        user.setEmail((String) userAttr.get("email"));
        user.setFavoriteCourses((Set<Course>) userAttr.get("favorite_courses"));
        var savedUser = userRepo.save(user);
        logger.info("User updated: " + savedUser.toString());
        return user;
    }
}
