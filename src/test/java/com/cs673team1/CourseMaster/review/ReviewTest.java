package com.cs673team1.CourseMaster.review;

import com.cs673team1.CourseMaster.course.Course;
import com.cs673team1.CourseMaster.user.Instructor;
import com.cs673team1.CourseMaster.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReviewTest {

    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewRepository reviewRepo;



    @Test
    public void testReview() {
        Review review = new Review();

        // test basic set method
        Integer id = 999;
        review.setId(id);
        assertEquals(id, review.getId());

        LocalDate localDate = LocalDate.now();
        review.setCourseDate(localDate);
        assertEquals(localDate, review.getCourseDate());

        Instructor instructor = new Instructor();
        review.setInstructor(instructor);
        assertEquals(instructor, review.getInstructor());

        String comment = "Hi! This is test comment";
        review.setComment(comment);
        assertEquals(comment, review.getComment());

        Float rating = 2.5f;
        review.setRating(rating);
        assertEquals(rating,review.getRating());

        User user = new User();
        review.setUser(user);
        assertEquals(user, review.getUser());
    }
}
