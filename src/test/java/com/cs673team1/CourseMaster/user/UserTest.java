package com.cs673team1.CourseMaster.user;

import com.cs673team1.CourseMaster.course.College;
import com.cs673team1.CourseMaster.course.Course;
import com.cs673team1.CourseMaster.course.MockData;
import com.cs673team1.CourseMaster.review.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class UserTest {

    @Autowired
    private MockData courseMockData;

    @Test
    void testUser() {
        // begin by testing basic user info
        User user = new User();
        user.setId(5);
        user.setoAuth2Id("unique-sso-id-123");
        user.setUserName("User Name");
        user.setEmail("user@email.com");
        user.setRole(User.Role.ROLE_STUDENT);

        assertEquals(5, user.getId());
        assertEquals("unique-sso-id-123", user.getoAuth2Id());
        assertEquals("User Name", user.getUserName());
        assertEquals("user@email.com", user.getEmail());
        assertEquals(User.Role.ROLE_STUDENT, user.getRole());
        assertNotEquals(User.Role.ROLE_ADMIN, user.getRole());

        // test fav courses
        Course course1 = new Course(College.MET, "CS", 999, "Test Course 1", 4,
                Course.Type.ONLINE, "test description", 0F);
        user.setFavoriteCourses(Set.of(course1));
        assertEquals(1, user.getFavoriteCourses().size());

        // test user reviews
        Review review1 = new Review("test review", 5F);
        user.setReviews(Set.of(review1));
        assertEquals(1, user.getReviews().size());
    }
}
