package com.cs673team1.CourseMaster.course;

import com.cs673team1.CourseMaster.user.User;
import com.cs673team1.CourseMaster.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Service
@RequestMapping("/api/favoritecourse")
public class FavoriteCourseController {

    @Autowired
    CourseService courseService;

    @Autowired
    UserService userService;

    @Autowired
    FavoriteCourseService favoriteCourseService;

    //Write general get method to retrieve favorite courses
    //My method should be retrieveAllFavoriteCourses
    //Top 5 should be a query parameter so the front can retrieve a different amount of courses
    //@RequestParams will take in the count and if not provide returns all popular courses.
    //If count is provided then return those in ascending order based on count
    //Exclude those courses not favorited by anyone.

    /**
     *
     * @param count the number of favorite courses
     * @return OK response
     */
    @GetMapping("/")
    public ResponseEntity retrieveAllFavoriteCourses (@RequestParam String count) {
        return new ResponseEntity(HttpStatus.OK);
    }

    /**
     *
     * @param req Http servlet request
     * @return the set of favorite courses.
     */
    @GetMapping("/me")
    public ResponseEntity<Set<Course>> retrieveMyFavoriteCourses(HttpServletRequest req) {
        User user = userService.getCurrentUser(req);
        if (user.getFavoriteCourses() == null) {
            user.setFavoriteCourses(new HashSet<>());
        }
        Set<Course> favoriteCourses = user.getFavoriteCourses();
        return new ResponseEntity<>(favoriteCourses, HttpStatus.OK);
    }

    /**
     *
     * @param course the course object
     * @param req Http servlet request
     * @return the specified user that save favorite course
     */
   @PostMapping("/user/{id}")
    public ResponseEntity<User> saveFavoriteCourseForUser(@Valid @RequestBody Course course, HttpServletRequest req) {
        User user = userService.getCurrentUser(req);
        user = favoriteCourseService.saveFavoriteCourse(user, course); //updates the set with new favorite course
       return new ResponseEntity<>(user, HttpStatus.CREATED);
   }

    /**
     *
     * @param req Http servlet request
     * @param id the id of the course
     * @return the specified user that remove favorite course
     */
   @DeleteMapping("/user/{id}")
    public ResponseEntity<User> removeFavoriteCourseForUser(HttpServletRequest req, @PathVariable int id) {
        User user = userService.getCurrentUser(req);
        Course course = courseService.getCourseByID(id);
        user = favoriteCourseService.deleteFavoriteCourse(user, course);
        return new ResponseEntity<>(user, HttpStatus.OK);
   }
}
