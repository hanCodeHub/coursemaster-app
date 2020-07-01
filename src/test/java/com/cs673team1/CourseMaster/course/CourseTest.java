package com.cs673team1.CourseMaster.course;

import com.cs673team1.CourseMaster.review.Review;
import com.cs673team1.CourseMaster.user.Instructor;
import com.cs673team1.CourseMaster.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CourseTest {

    @Test
    void testCourse() {
        // begin by testing basic course info
        var course1 = new Course();
        course1.setId(98);
        course1.setCollege(College.MET);
        course1.setDepartment("CS");
        course1.setCourseNumber(101);
        course1.setTitle("Test title");
        course1.setCredits(4);
        course1.setType(Course.Type.IN_PERSON);
        course1.setDescription("Test description");
        course1.setAverageRating(3F);

        assertEquals(98, course1.getId());
        assertEquals(College.MET, course1.getCollege());
        assertEquals("CS", course1.getDepartment());
        assertEquals(101, course1.getCourseNumber());
        assertEquals("Test title", course1.getTitle());
        assertEquals(4, course1.getCredits());
        assertEquals(Course.Type.IN_PERSON, course1.getType());
        assertEquals("Test description", course1.getDescription());
        assertEquals(3F, course1.getAverageRating());

        // test full-title string
        assertEquals("MET CS 101 - Test title", course1.getFullTitle());
        course1.setType(Course.Type.ONLINE);
        assertEquals("MET CS 101 - Test title (Online)", course1.getFullTitle());
        course1.setType(Course.Type.HYBRID);
        assertEquals("MET CS 101 - Test title (Blended/eLive)", course1.getFullTitle());

        // test user reviews
        Review review1 = new Review("test review", 5F);
        course1.setReviews(Set.of(review1));
        assertEquals(1, course1.getReviews().size());

        // test instructors for this course
        Instructor instructor = new Instructor();
        instructor.setName("Test");
        instructor.setCourses(List.of(course1));
        course1.setInstructors(Set.of(instructor));
        assertEquals(1, course1.getInstructors().size());

        // test users that favorite this course
        User user1 = new User("unique-oAuth2-123456", "user name",
                "user@email.com", User.Role.ROLE_STUDENT);
        course1.setUsers(Set.of(user1));
        assertEquals(1, course1.getUsers().size());
    }
}
