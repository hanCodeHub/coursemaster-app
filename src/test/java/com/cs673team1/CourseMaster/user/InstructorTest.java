package com.cs673team1.CourseMaster.user;

import com.cs673team1.CourseMaster.course.College;
import com.cs673team1.CourseMaster.course.Course;
import com.cs673team1.CourseMaster.review.Review;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InstructorTest {

    @Test
    void testInstructor() {
        // begin by testing basic instructor info
        Instructor instructor = new Instructor();
        instructor.setId(5);
        instructor.setName("Prof. Someone");

        assertEquals(5, instructor.getId());
        assertEquals("Prof. Someone", instructor.getName());

        // test courses taught by instructor
        Course course1 = new Course(College.MET, "CS", 999, "Test Course 1", 4,
                Course.Type.ONLINE, "test description", 0F);
        instructor.setCourses(List.of(course1));
        assertEquals(1, instructor.getCourses().size());

        // test user reviews received by instructor
        Review review1 = new Review("test review", 5F);
        instructor.setReviews(List.of(review1));
        assertEquals(1, instructor.getReviews().size());
    }
}
