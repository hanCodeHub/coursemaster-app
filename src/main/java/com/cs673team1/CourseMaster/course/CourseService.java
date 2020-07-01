package com.cs673team1.CourseMaster.course;

import com.cs673team1.CourseMaster.course.CourseSearchAndSortService.SortMode;
import com.cs673team1.CourseMaster.review.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Provides the service class for the course catalogue.
 */
@Service
public class CourseService {

    /** Course repository for retrieving courses from the database. */
    @Autowired
    private CourseRepository courseRepository;

    /** Service for searching and sorting the course catalogue. */
    @Autowired
    private CourseSearchAndSortService courseSearchAndSortService;

    /** Review service for retrieving and processing review information. */
    @Autowired
    private ReviewService reviewService;

    /**
     * Returns the total number of courses stored in the database.
     * @return The total number of courses in the database.
     */
    public Long getCourseCount() {
        return this.courseRepository.count();
    }

    /**
     * Returns the total number of pages produced when paginating the course
     * catalogue.
     * @return The total number of available course pages.
     */
    public Integer getCoursePageCount() {
        return CourseSearchAndSortService.calcPageCount(
                (int) this.courseRepository.count());
    }

    /**
     * Returns the entire course catalogue stored in the database.
     * @return The entire course catalogue from the database.
     */
    public List<Course> getAllCourses() {
        var courses = this.courseRepository.findAll();
        courses.forEach(this::setCourseAverageRating);
        return courses;
    }

    /**
     * Returns a paginated list of courses for the specified page, using the
     * specified sort options.
     * @param page The 1-based page number for which to return the paginated
     *             search results.
     * @param sortMode The sort mode to use for sorting the course catalogue.
     * @param sortDirection Whether to sort in ascending or descending order.
     * @return A paginated list of courses, sorted according to the specified
     *  sort mode and direction.
     * @see CourseSearchAndSortService#paginateAndSort(List, int, SortMode,
     *  Sort.Direction)
     */
    public List<Course> getCoursesByPage(int page, SortMode sortMode,
                                         Sort.Direction sortDirection) {
        var courses = this.courseSearchAndSortService.paginateAndSort(
                this.getAllCourses(), page, sortMode, sortDirection);
        courses.forEach(this::setCourseAverageRating);
        return courses;
    }

    /**
     * Searches for a course with the specified unique id.
     * @param id The unique identifier of a course to search for.
     * @return The course with the specified id.
     */
    public Course getCourseByID(Integer id) {
        Optional<Course> course = this.courseRepository.findById(id);
        if (course.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "course with id '" + id + "' not found");
        }
        this.setCourseAverageRating(course.get());
        return course.get();
    }

    /**
     * Searches the course catalogue for a list of courses with a course title
     * similar to the specified value.
     * @param title The title of the course to search for.
     * @return A list of courses with a course title similar to the specified
     *  value.
     */
    public List<Course> getCourseByTitle(String title) {
        List<Course> courses = this.courseRepository.findByTitle(title);
        if (courses.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "course not found");
        }
        return courses;
    }

    /**
     * Searches the course catalogue for a list of courses using the specified
     * keywords.
     * @param keywords A concatenated string of keywords.
     * @param page The 1-based page number for which to return the paginated
     *             search results.
     * @param sortMode The sort mode to use for sorting the course catalogue.
     * @param sortDirection Whether to sort in ascending or descending order.
     * @return A paginated list of courses based on the specified search
     *  keywords and page.
     * @throws ResponseStatusException
     *  if search keywords are not valid;
     *  if search did not produce any results; or
     *  if requested page is out-of-bounds.
     * @see CourseSearchAndSortService#searchByKeywords(String, int, SortMode,
     *  Sort.Direction)
     */
    public Map<String, Object> searchCoursesByKeywords(
            String keywords, int page, SortMode sortMode,
            Sort.Direction sortDirection) {
        return this.courseSearchAndSortService.searchByKeywords(
                keywords, page, sortMode, sortDirection);
    }

    /**
     * Calculates and sets the average rating on the specified course.
     * @param course The course for which to calculate and set the average
     *               rating.
     */
    private void setCourseAverageRating(Course course) {
        course.setAverageRating(
                reviewService.calcAverageRating(course.getId()));
    }

}
