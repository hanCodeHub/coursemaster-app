package com.cs673team1.CourseMaster.course;

import com.cs673team1.CourseMaster.user.User;
import com.cs673team1.CourseMaster.user.UserRepository;
import com.cs673team1.CourseMaster.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@Service
public class FavoriteCourseService {

    @Autowired
    UserService userService;

    @Autowired
    CourseService courseService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    /**
     *
     * @param user the specified user.
     * @param course the specified course.
     * @return the specified user that save favorite course.
     */
    public User saveFavoriteCourse(User user, Course course) {
        if (user.getFavoriteCourses() == null) {
            user.setFavoriteCourses(new HashSet<>());
        }

        user.getFavoriteCourses().add(course);
        course.getUsers().add(user);
        userRepository.save(user);
        courseRepository.save(course);

        return user;
    }

    /**
     *
     * @param user the specified user.
     * @param course the specified course.
     * @return the specified user that delete favorite course.
     */
    public User deleteFavoriteCourse(User user, Course course) {
        Set<Course> favoriteCourseSet = user.getFavoriteCourses();
        if (favoriteCourseSet.contains(course)) {
            favoriteCourseSet.remove(course);
            course.getUsers().remove(user);
            userRepository.save(user);
            courseRepository.save(course);
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "course does not exist");
        }
        return user;
    }

    /*Table for Thursday June 15th 2020
    public List<Course> getFavoriteCourses(int count) {

    }
    */

}
