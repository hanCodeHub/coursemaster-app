package com.cs673team1.CourseMaster.course;

import com.cs673team1.CourseMaster.course.CourseSearchAndSortService.SortMode;
import com.cs673team1.CourseMaster.review.Review;
import com.cs673team1.CourseMaster.review.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Provides the controller class for the course catalogue.
 */
@RestController
@RequestMapping("api/courses")
public class CourseController {

    /** Course service for retrieving and processing course information. */
    @Autowired
    private CourseService courseService;

    /** Review service for retrieving and processing review information. */
    @Autowired
    private ReviewService reviewService;


    /**
     * Returns the total number of courses stored in the database.
     * @return The total number of courses in the database.
     * @see CourseService#getCourseCount()
     */
    @GetMapping("/count")
    public ResponseEntity<Long> retrieveCourseCount() {
        long courseCount = this.courseService.getCourseCount();
        return new ResponseEntity<>(courseCount, HttpStatus.OK);
    }

    /**
     * Returns the total number of pages produced when paginating the course
     * catalogue.
     * @return The total number of available course pages.
     * @see CourseService#getCoursePageCount()
     */
    @GetMapping("/pages/count")
    public ResponseEntity<Integer> retrieveCoursePageCount() {
        int pageCount = this.courseService.getCoursePageCount();
        return new ResponseEntity<>(pageCount, HttpStatus.OK);
    }

    /**
     * Returns the entire course catalogue stored in the database.
     * @return The entire course catalogue from the database.
     * @see CourseService#getAllCourses()
     */
    @GetMapping("")
    public ResponseEntity<List<Course>> retrieveAllCourses() {
        var courses = this.courseService.getAllCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    /**
     * Returns a paginated list of courses for the specified page, using the
     * specified sort options.
     * @param page The 1-based page number for which to return the paginated
     *             search results.
     * @param sort The sort mode to use for sorting the course catalogue.
     * @param direction Whether to sort in ascending or descending order.
     * @return A paginated list of courses, sorted according to the specified
     *  sort mode and direction.
     * @see CourseService#getCoursesByPage(int, SortMode, Sort.Direction)
     */
    @GetMapping("/pages/{page}")
    public ResponseEntity<List<Course>> retrieveCoursesByPage(
            @PathVariable int page,
            @RequestParam(required=false) SortMode sort,
            @RequestParam(required=false) Sort.Direction direction) {
        var courses = this.courseService.getCoursesByPage(
                page,
                (sort == null) ? SortMode.DEFAULT : sort,
                (direction == null) ? Sort.Direction.ASC : direction);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    /**
     * Searches for a course with the specified unique id.
     * @param id The unique identifier of a course to search for.
     * @return The course with the specified id.
     * @see CourseService#getCourseByID(Integer)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Course> retrieveCourseById(@PathVariable int id) {
        Course course = this.courseService.getCourseByID(id);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    /**
     * Searches the course catalogue for a list of courses with a course title
     * similar to the specified value.
     * @param value The title of the course to search for.
     * @return A list of courses with a course title similar to the specified
     *  value.
     * @see CourseService#getCourseByTitle(String)
     */
    @GetMapping("/title={value}")
    public ResponseEntity<List<Course>> retrieveCourseByTitle(
            @PathVariable String value) {
        var courses = this.courseService.getCourseByTitle(value);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    /**
     * Searches the course catalogue for a list of courses using the specified
     * keywords.
     * @param keywords A concatenated string of keywords.
     * @param page The 1-based page number for which to return the paginated
     *             search results.
     * @param sort The sort mode to use for sorting the course catalogue.
     * @param direction Whether to sort in ascending or descending order.
     * @return A paginated list of courses based on the specified search
     *  keywords and page.
     * @see CourseService#searchCoursesByKeywords(String, int, SortMode,
     *  Sort.Direction)
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchCoursesByKeywords(
            @RequestParam String keywords,
            @RequestParam(required=false) Integer page,
            @RequestParam(required=false) SortMode sort,
            @RequestParam(required=false) Sort.Direction direction) {

        var searchResults = this.courseService.searchCoursesByKeywords(
                keywords,
                (page == null) ? 1 : page,
                (sort == null) ? SortMode.DEFAULT : sort,
                (direction == null) ? Sort.Direction.ASC : direction);
        return new ResponseEntity<>(searchResults, HttpStatus.OK);
    }

    /**
     * Gets course review by id.
     * @param courseId Id assigned automatically when user is created.
     * @return List of reviews for specified course.
     */
    @GetMapping("/{courseId}/reviews")
    public ResponseEntity<List<Review>> retrieveCourseReviewById(
            @PathVariable int courseId) {
        var reviews = reviewService.getReviewsByCourseId(courseId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    /**
     * Posts new review by user and saves to database.
     * @param courseId User Id.
     * @param review New review to be added.
     * @return Returns built URI.
     */
    @PostMapping("/{courseId}/reviews")
    public ResponseEntity<Review> createCourseReview(
            @PathVariable int courseId,
            @Valid @RequestBody Review review) {
        var savedReview = reviewService.createReviewForCourse(courseId,
                                                              review);
        return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
    }

    /**
     * Update specified course review.
     * @param courseId Id for course to be updated.
     * @param reviewId Id for review to be updated.
     * @param updatedReview Updated review.
     * @return Returns review with changes.
     */
    @PutMapping("/{courseId}/reviews/{reviewId}")
    public ResponseEntity<Review> updateCourseReview(
            @PathVariable int courseId,
            @PathVariable int reviewId,
            @RequestBody Review updatedReview) {
        var savedReview = reviewService.updateReview(reviewId, courseId,
                updatedReview);
        return new ResponseEntity<>(savedReview, HttpStatus.ACCEPTED);
    }

    /**
     * Delete specified course review.
     * @param courseId Id for course of deleted review.
     * @param reviewId Id for review to be deleted.
     */
    @DeleteMapping("/{courseId}/reviews/{reviewId}")
    public void deleteCourseReview(
            @PathVariable int courseId,
            @PathVariable int reviewId) {
        reviewService.deleteReview(reviewId);
    }
}
