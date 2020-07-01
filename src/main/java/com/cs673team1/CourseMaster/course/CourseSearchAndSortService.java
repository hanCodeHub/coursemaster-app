package com.cs673team1.CourseMaster.course;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides the searching by keyword(s) and sorting functionality of the course
 * catalogue.
 */
@Service
public class CourseSearchAndSortService {

    /**
     * Defines the sort mode to use for sorting the course catalogue.
     */
    public enum SortMode {
        /**
         * Specifies that the course catalogue should be sorted using the
         * default sort ordering. When default sorting is used, the course
         * catalogue is sorted by college, department, and course number.
         */
        DEFAULT,
        /**
         * Specifies that the course catalogue should be sorted based on
         * average course rating.
         */
        RATING,
        /**
         * Specifies that the course catalogue should be sorted based on total
         * number of course reviews.
         */
        REVIEW_COUNT;
    }

    /**
     * Defines number of courses to return per page when producing a paginated
     * list of courses.
     */
    public static final int COURSES_PER_PAGE = 10;

    /**
     * Defines the minimum allowed page when paginating a list of courses.
     */
    public static final int MIN_PAGE = 1;

    /** Defines the default sort comparator. */
    private static final Comparator<Course> DEFAULT_SORT =
            Comparator.comparing(Course::getCollege)
            .thenComparing(Course::getDepartment)
            .thenComparing(Course::getCourseNumber);

    /** Defines the sort-by-rating comparator. */
    private static final Comparator<Course> SORT_BY_RATING =
            Comparator.comparing(Course::getAverageRating);

    /** Defines the sort-by-review-count comparator. */
    private static final Comparator<Course> SORT_BY_REVIEW_COUNT =
            Comparator.comparing(Course::getReviewCount);

    /** Course service for routing certain requests. */
    @Autowired
    private CourseService courseService;


    /**
     * Computes the number of expected pages when paginated the specified
     * desired number of courses.
     * @param desiredCourseCount The number of courses to paginate.
     * @return The number of pages resulting from pagination.
     */
    public static int calcPageCount(int desiredCourseCount) {
        return (desiredCourseCount / COURSES_PER_PAGE) // whole pages
            + (desiredCourseCount % COURSES_PER_PAGE == 0 ? 0 : 1); // partial
    }

    /**
     * Paginates and sorts the specified list of courses based on the specified
     * page number and sort options.
     * @param courseList The list of courses to paginate.
     * @param page The 1-based page number for which to return the paginated
     *             search results.
     * @param sortMode The sort mode to use for sorting the course catalogue.
     * @param sortDirection Whether to sort in ascending or descending order.
     * @return A paginated list of courses, sorted according to the specified
     *  sort mode and direction.
     * @throws ResponseStatusException if requested page is out-of-bounds.
     */
    public List<Course> paginateAndSort(List<Course> courseList, int page,
                                        SortMode sortMode,
                                        Sort.Direction sortDirection) {
        if (courseList.size() == 0) {
            return courseList;
        }
        if (page < MIN_PAGE || page > calcPageCount(courseList.size())) {
            this.throwInvalidPageNumber();
        }

        var paginatedList = new ArrayList<>(courseList);
        paginatedList.sort(this.getSortComparator(sortMode, sortDirection));

        if (paginatedList.size() < COURSES_PER_PAGE) {
            return paginatedList;
        }

        int start = (page - 1) * COURSES_PER_PAGE;
        int end = Math.min(page * COURSES_PER_PAGE, paginatedList.size());
        return paginatedList.subList(start, end);
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
     */
    public Map<String, Object> searchByKeywords(String keywords, int page,
                                                SortMode sortMode,
                                                Sort.Direction sortDirection) {
        if (page < MIN_PAGE) {
            this.throwInvalidPageNumber();
        }

        var searchResults = this.getSearchResults(keywords);
        var paginatedResults = new HashMap<String, Object>();
        paginatedResults.put("searchResults", this.paginateAndSort(
                searchResults, page, sortMode, sortDirection));
        paginatedResults.put("courseCount", searchResults.size());

        return paginatedResults;
    }

    /**
     * Searches the course catalogue for a list of courses using the specified
     * keywords.
     * @param keywordsQuery A concatenated string of keywords.
     * @return A list of courses based on the specified search keywords.
     * @throws ResponseStatusException if search keywords are not valid.
     */
    private List<Course> getSearchResults(String keywordsQuery) {
        if (keywordsQuery.strip().equals("")) {
            this.throwInvalidSearchKeywords();
        }

        var keywords = StringUtils.split(keywordsQuery.toLowerCase(), " ");
        var allCourses = this.courseService.getAllCourses();
        var searchResults = new ArrayList<Course>();

        for (Course course : allCourses) {
            String fullTitle = course.getFullTitle().toLowerCase();
            int keywordMatches = Arrays.stream(keywords)
                    .mapToInt(keyword -> fullTitle.contains(keyword) ? 1 : 0)
                    .sum();

            if (keywordMatches == keywords.length) {
                searchResults.add(course);
            }
        }

        return searchResults;
    }

    /**
     * Returns a comparator to use for sorting the course list, based on the
     * specified sort mode and direction.
     * @param sortMode The sort mode to use for sorting the course catalogue.
     * @param sortDirection Whether to sort in ascending or descending order.
     * @return A comparator to use for sorting the course list.
     */
    private Comparator<Course> getSortComparator(SortMode sortMode,
            Sort.Direction sortDirection) {
        Comparator<Course> comparator;
        switch (sortMode) {
            case RATING:
                comparator = SORT_BY_RATING;
                break;
            case REVIEW_COUNT:
                comparator = SORT_BY_REVIEW_COUNT;
                break;
            case DEFAULT:
            default:
                comparator = DEFAULT_SORT;
                break;
        }
        return sortDirection.isDescending()
                ? comparator.reversed() : comparator;
    }

    /**
     * Helper method that throws a ResponseStatusException with a BAD_REQUEST
     * status and an "invalid keywords entered for search query" message.
     * <br/>
     * This method should be called whenever requested keywords are not valid
     * (for example, an empty keyword string).
     */
    private void throwInvalidSearchKeywords() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "invalid keywords entered for search query");
    }

    /**
     * Helper method that throws a ResponseStatusException with a BAD_REQUEST
     * status and an "invalid page number entered for course pagination"
     * message.
     * <br/>
     * This method should be called whenever requested page for search
     * results is out-of-bounds.
     */
    private void throwInvalidPageNumber() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "invalid page number entered for course pagination");
    }

}
