package com.cs673team1.CourseMaster.user;

import com.cs673team1.CourseMaster.review.ReviewRepository;
import com.cs673team1.CourseMaster.review.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * Creates UserService.
     */
    @Autowired
    private UserService userService;

    /**
     *
     * @param req Request from Http Servlet
     * @return User and HttpStatus OK
     */
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(HttpServletRequest req) {
        var user = userService.getCurrentUser(req);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     *
     * @param userId unique id of a user
     * @return User found
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        var user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
