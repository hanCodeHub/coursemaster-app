package com.cs673team1.CourseMaster.review;

import com.cs673team1.CourseMaster.course.College;
import com.cs673team1.CourseMaster.course.Course;
import com.cs673team1.CourseMaster.course.CourseRepository;
import com.cs673team1.CourseMaster.user.User;
import com.cs673team1.CourseMaster.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;

    @Autowired
    CourseRepository courseRepo;

    @Autowired
    ReviewRepository reviewRepo;

    @Autowired
    UserRepository userRepository;

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
    public void getCourseReviewByIdTest() {
        Optional<Course> course = courseRepo.findById(courseId);
        var reviewsFromRepo = course.get().getReviews();
        var reviewsFromService = reviewService.getReviewsByCourseId(courseId);
        ArrayList<Integer> reviewIdList = new ArrayList();
        ArrayList<Integer> reviewIdList2 = new ArrayList();

        // Check if sets are same size
        assertEquals(reviewsFromRepo
                .size(), reviewService.getReviewsByCourseId(courseId).size());

        reviewsFromRepo.forEach(r -> reviewIdList.add(r.getId()));
        reviewsFromService.forEach(r -> reviewIdList2.add(r.getId()));
        // Test both lists have same reviewId's
        reviewIdList.forEach(r -> assertTrue(reviewIdList2.contains(r)));
    }

    @Test
    public void updateReviewTest() {
        ArrayList<Review> reviewList = new ArrayList<>();
        reviewList.addAll(reviews);
        Review review = reviewList.get(0);
        Course course = review.getCourse();
        var updatedReview = new Review("My super fake review 9000", 5.0F);

        assertNotEquals(updatedReview.getId(), review.getId());
        assertNotEquals(updatedReview.getCourse(), review.getCourse());
        review = reviewService.updateReview(review.getId(), course.getId(), updatedReview);
        assertEquals(updatedReview.getId(), review.getId());
        assertEquals(updatedReview.getCourse(), review.getCourse());
    }

    @Test
    public void deleteReviewTest() {
        Optional<Course> course = courseRepo.findById(courseId);
        var reviewsFromRepo = course.get().getReviews();

        assertEquals(reviewsFromRepo.size(),reviews.size());
        reviews.forEach(r -> reviewService.deleteReview(r.getId()));

        course = courseRepo.findById(courseId);
        reviewsFromRepo = course.get().getReviews();
        assertEquals(reviewsFromRepo.size(),0);
    }

    @Test
    public void saveReviewTest() {
        Review review = null;
        // test null review
        var exception = assertThrows(ResponseStatusException.class,
                () -> reviewService.saveReview(review));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

        Set<Review> reviewSet = new HashSet<>();
        int reviewsCount = (int)reviewRepo.count();
        User user = userRepository.getOne(1);

        Review newReview = new Review("save FAKE review",4.0F);
        reviewService.saveReview(newReview);
        reviewSet.add(newReview);

        // test not exist review id
        exception = assertThrows(ResponseStatusException.class,
                () -> reviewService.getReviewByID(99999));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        int id = reviewService.getReviewByID(reviewsCount+reviewSet.size()).getId();

        // test not exist user id
        exception = assertThrows(ResponseStatusException.class,
                () -> reviewService.getReviewsByUserId(99999));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        assertEquals(id,reviewsCount+reviewSet.size());
        assertEquals(reviewService.getReviewsByUserId(user.getId()).size(),reviewSet.size());


        Review newReview2 = new Review("save FAKE review2",4.0F);
        reviewSet.add(newReview2);
        reviewRepo.save(newReview2);
        newReview2.setUser(user);
        assertEquals(reviewRepo.count(),reviewsCount+reviewSet.size());

        reviewRepo.delete(newReview);
        reviewRepo.delete(newReview2);
    }

    @Test
    public void createReviewForCourseTest() {
        Review newReview = new Review("save FAKE review",4.0F);
        Course course = new Course(College.MET, "CS", 999, "test name",
                4, Course.Type.ONLINE, "test description", 0F);
        int id = (int)courseRepo.count()-1;
        courseRepo.save(course);
        newReview.setUser(userRepository.getOne(1));
        reviewRepo.save(newReview);
        reviewService.createReviewForCourse(id,newReview);

        // test one user add one more review for a course
        var exception = assertThrows(ResponseStatusException.class,
                () -> reviewService.createReviewForCourse(id,newReview));
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }
}