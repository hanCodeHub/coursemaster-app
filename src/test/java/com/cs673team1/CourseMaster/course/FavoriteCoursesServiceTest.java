package com.cs673team1.CourseMaster.course;

import com.cs673team1.CourseMaster.user.User;
import com.cs673team1.CourseMaster.user.UserRepository;
import com.cs673team1.CourseMaster.user.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FavoriteCoursesServiceTest {

    @Autowired
    FavoriteCourseService favoriteCourseService;

    @Autowired
    private MockData courseMockData;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    @BeforeEach
    void setUp() {
        courseMockData.setUp();
    }

    @AfterEach
    void tearDown() {
        courseMockData.tearDown();
        userRepo.deleteAll();
    }

    @Test
    public void saveFavoriteCourseTest() throws Exception {
        User user = new User("saveFavoriteCourseTest","vvc","vishnu@gmail.com",User.Role.ROLE_STUDENT);
        List<Course> courseList = courseMockData.testCourses;
        user = favoriteCourseService.saveFavoriteCourse(user, courseList.get(0));
        assertEquals(user.getFavoriteCourses().size(), 1);
    }

    @Test
    public void deleteFavoriteCourseTest() throws Exception {
        User user = new User("deleteFavoriteCourseTest","vvc","vishnu@gmail.com",User.Role.ROLE_STUDENT);
        List<Course> courseList = courseMockData.testCourses;
        user = favoriteCourseService.saveFavoriteCourse(user, courseList.get(1));
        user = favoriteCourseService.deleteFavoriteCourse(user, courseList.get(1));
        assertEquals(user.getFavoriteCourses().size(), 0);

    }

}
