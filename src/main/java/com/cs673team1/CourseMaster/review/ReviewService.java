package com.cs673team1.CourseMaster.review;

import com.cs673team1.CourseMaster.course.Course;
import com.cs673team1.CourseMaster.course.CourseRepository;
import com.cs673team1.CourseMaster.course.CourseService;
import com.cs673team1.CourseMaster.user.User;
import com.cs673team1.CourseMaster.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ReviewService {

    /**
     * Injects UserRepository.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Injects ReviewRepository.
     */
    @Autowired
    private ReviewRepository reviewRepository;

    /**
     * Injects CourseService.
     */
    @Autowired
    private CourseService courseService;

    /**
     * Injects CourseRepository.
     */
    @Autowired
    private CourseRepository courseRepository;

    /**
     *
     * @param courseId ID of course to be searched
     * @return Returns a list of reviews from specified course
     */
    public List<Review> getReviewsByCourseId(final int courseId) {
        Course course = verifyCourseExists(courseId);
        return course.getReviews();
    }

    /**
     *
     * @param userId unique id of user
     * @return all reviews that a user wrote
     */
    public Set<Review> getReviewsByUserId(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with id " + userId + " not found");
        }
        return reviewRepository.findByUserId(userId);
    }

    /**
     *
     * @param reviewId Id for review to be found
     * @return Returns single review
     */
    public Review getReviewByID(final int reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if (review.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "review with id '" + reviewId + "' not found");
        }
        return review.get();
    }

    /**
     *
     * @param courseId Course id for course to be reviewed
     * @param newReview Review object to be created
     * @return Returns saved review
     */
    public Review createReviewForCourse(final int courseId,
                                        final Review newReview) {
        Course course = verifyCourseExists(courseId);
        int userId = newReview.getUser().getId();

        for (Review review:course.getReviews()) {
            if (review.getUser().getId() == userId){
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "You can only make one review per course");
            }
        }

        newReview.setCourse(course);
        courseRepository.save(course);
        return reviewRepository.save(newReview);
    }

    /**
     *
     * @param reviewId Id for review that requires an update
     * @param courseId Id for course that requires an update
     * @param newReview New review for update
     * @return Returns updated review object
     */
    public Review updateReview(final int reviewId, final int courseId,
                               final Review newReview) {

        verifyReviewExists(reviewId);
        Course course = verifyCourseExists(courseId);
        
        newReview.setId(reviewId);
        newReview.setCourse(course);

        reviewRepository.save(newReview);
        return newReview;
    }

    /**
     *
     * @param reviewId Id for review that requires an update
     */
    public void deleteReview(final int reviewId) {
        Review review = verifyReviewExists(reviewId);
        reviewRepository.delete(review);
    }

    /**
     *
     * @param review Review that needs to be saved
     * @return Returns saved review object
     */
    public Review saveReview(final Review review) {
        if (review == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        reviewRepository.save(review);
        return review;
    }

    /**
     * Calculates the average rating of all reviews on a course.
     * @param courseId Id of course to be evaluated
     * @return Returns average of all reviews
     */
    public Float calcAverageRating(final Integer courseId) {
        var reviews = getReviewsByCourseId(courseId);
        float ratings = reviews.stream()
                .map(Review::getRating)
                .reduce(Float::sum).orElse(0.0F);
        // check for divide by zero
        return ratings == 0.0F ? 0 : ratings / reviews.size();
    }

    /**
     * Verifies if a course exists.
     * @param courseId Id of course for verification
     * @return Returns course object
     */
    public Course verifyCourseExists(final int courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Course with id " + courseId + " not found");
        }
        return courseOptional.get();
    }

    /**
     * Verifies if a review exists.
     * @param reviewId Id of course for verification
     * @return Returns review object
     */
    public Review verifyReviewExists(final int reviewId) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (reviewOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Review with id " + reviewId + " not found");
        }
        return reviewOptional.get();
    }
}
