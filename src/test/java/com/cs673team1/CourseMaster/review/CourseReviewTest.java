package com.cs673team1.CourseMaster.review;

import com.cs673team1.CourseMaster.course.College;
import com.cs673team1.CourseMaster.course.Course;
import com.cs673team1.CourseMaster.course.CourseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CourseReviewTest {

    @Autowired
    ReviewService reviewService;

    @Autowired
    CourseRepository courseRepo;

    @Autowired
    ReviewRepository reviewRepo;

    private Integer courseId;

    private Set<Review> reviews;

    private static final String UNIQUE_COURSE_NAME = "__-Unknown@#$Course*()>?";

    @BeforeEach
    void setUp() {
        var review1 = new Review("My fake review 998.", 3.0F);
        var review2 = new Review("My fake review 997.", 4.0F);
        var review3 = new Review("My fake review 996.", 5.0F);
        review1.setCourseDate(LocalDate.of(2019, 10, 15));
        review2.setCourseDate(LocalDate.of(2017, 3, 26));
        review3.setCourseDate(LocalDate.of(2018, 4, 2));
        reviews = Set.of(review1, review2, review3);

        // saves course with reviews
        var course = new Course(College.MET, "CS", 899, UNIQUE_COURSE_NAME,
                4, Course.Type.ONLINE, "This course has not be developed yet", 0F);

        reviews.forEach(r -> r.setCourse(course));
        course.setReviews(reviews);
        courseRepo.save(course);

        // retrieves auto generated courseId
        courseId = courseRepo.findByTitle(UNIQUE_COURSE_NAME).get(0).getId();
    }

    @AfterEach
    void tearDown() {
        courseRepo.deleteById(courseId);
        reviews = null;
    }

    @Test
    public void getCourseReviews() {
        var courseReviews = reviewService.getReviewsByCourseId(courseId);
        // tests for reviews count
        assertEquals(courseReviews.size(), reviews.size());

        List<String> reviewComments = reviews.stream()
                .map(Review::getComment)
                .collect(Collectors.toList());

        // tests for reviews objects
        courseReviews.stream()
                .map(Review::getComment)
                .forEach(e -> assertTrue(reviewComments.contains(e)));
    }

    @Test
    public void calcAverageRating() {
        float averageRating = reviewService.calcAverageRating(courseId);
        assertEquals(4.0F, averageRating);
    }
}
