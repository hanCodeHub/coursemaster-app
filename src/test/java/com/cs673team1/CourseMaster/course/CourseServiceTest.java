package com.cs673team1.CourseMaster.course;

import com.cs673team1.CourseMaster.course.CourseSearchAndSortService.SortMode;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CourseServiceTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private MockData courseMockData;


    @BeforeEach
    void setUp() {
        courseMockData.setUp();
    }

    @AfterEach
    void tearDown() {
        courseMockData.tearDown();
    }


    // test that number of courses obtained by courseService matches what's in the database
    @Test
    void getCourseCount() {
        assertEquals(MockData.NUMBER_OF_COURSES, courseService.getCourseCount());
    }

    // test number of course pages
    @Test
    void getCoursePageCount() {
        assertEquals(MockData.NUMBER_OF_COURSE_PAGES, courseService.getCoursePageCount());
    }

    // test course pagination with sorting
    @Test
    void getCoursesByPageTest() {
        // get sorted list of expected courses
        var expectedCourses = new ArrayList<>(courseMockData.testCourses);
        expectedCourses.sort(Comparator.comparing(Course::getCollege)
                .thenComparing(Course::getDepartment)
                .thenComparing(Course::getCourseNumber));
        this.testPaginationAndSortHelper(expectedCourses, SortMode.DEFAULT,
                Sort.Direction.ASC);
    }

    // test course pagination with sorting
    @Test
    void getCoursesByPageWithSortingTest() {
        // test sort-by-rating
        var expectedCourses = new ArrayList<>(courseMockData.testCourses);
        expectedCourses.sort(Comparator.comparing(Course::getAverageRating));
        this.testPaginationAndSortHelper(
                expectedCourses, SortMode.RATING, Sort.Direction.ASC);

        // test sort-by-review-count descending
        expectedCourses = new ArrayList<>(courseMockData.testCourses);
        expectedCourses.sort(Comparator.comparing(Course::getReviewCount).reversed());
        this.testPaginationAndSortHelper(
                expectedCourses, SortMode.REVIEW_COUNT, Sort.Direction.DESC);
    }

    // helper for: getCoursesByPageTest and getCoursesByPageWithSortingTest
    void testPaginationAndSortHelper(List<Course> expectedCourses,
                                     SortMode sortMode,
                                     Sort.Direction sortDirection) {
        //
        for (int i = 1; i <= MockData.NUMBER_OF_COURSE_PAGES; i++) {
            var courses = courseService.getCoursesByPage(i, sortMode, sortDirection);

            // first test for expected number of courses on the page
            //  - for first N pages before last, we expect 10 courses each
            //  - for last page, we expect remainder of courses
            int expectedCourseCount = (i == MockData.NUMBER_OF_COURSE_PAGES
                    ? MockData.NUMBER_OF_COURSES % CourseSearchAndSortService.COURSES_PER_PAGE
                    : CourseSearchAndSortService.COURSES_PER_PAGE);
            assertEquals(expectedCourseCount, courses.size());

            // next test expected ordering according to sort mode and direction
            for (var course : courses) {
                var expectedCourse = expectedCourses.remove(0); // pop first course in sorted list
                assertEquals(expectedCourse.getCourseNumber(), course.getCourseNumber());
                assertEquals(expectedCourse.getCollege(), course.getCollege());
                assertEquals(expectedCourse.getDepartment(), course.getDepartment());
            }
        }
    }

    // test that all courses returned equals expected value
    @Test
    void getAllCourses() {
        var allCourses = courseService.getAllCourses();
        assertEquals(MockData.NUMBER_OF_COURSES, allCourses.size());
    }

    // test course retrieval by id
    @Test
    void getCourseByID() {
        // get real course first
        var mockCourse = courseMockData.testCourses.get(0);
        var course = courseService.getCourseByID(mockCourse.getId());
        assertEquals(course.getId(), mockCourse.getId());
        assertEquals(course.getCollege(), mockCourse.getCollege());
        assertEquals(course.getDepartment(), mockCourse.getDepartment());
        assertEquals(course.getCourseNumber(), mockCourse.getCourseNumber());

        // attempt to get course that does not exist
        var exception = assertThrows(ResponseStatusException.class,
                () -> courseService.getCourseByID(99999));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    // test course retrieval by title
    @Test
    void getCourseByTitle() {
        // expect 2 values, from MET CS 102 and CAS CS 102
        var courses = courseService.getCourseByTitle("number 102");
        assertEquals(2, courses.size());
        assertEquals("Course number 102", courses.get(0).getTitle());
        assertEquals("Course number 102", courses.get(1).getTitle());

        // attempt to search by title that does not exist
        var exception = assertThrows(ResponseStatusException.class,
                () -> courseService.getCourseByTitle("akhklasfh sjahf kjasdhfjkshf"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    // test course search by keywords for "valid" inputs
    @Test
    void searchCoursesByKeywordsTest() {
        // search by title using one or multiple keywords
        this.searchCoursesByKeywordsTest("course 101", 2);
        this.searchCoursesByKeywordsTest("course", MockData.NUMBER_OF_COURSES);

        // search by college/department/course-number
        this.searchCoursesByKeywordsTest("MET", MockData.NUM_MET_CS);
        this.searchCoursesByKeywordsTest("CAS", MockData.NUM_CAS_CS);
        this.searchCoursesByKeywordsTest("CS", MockData.NUMBER_OF_COURSES);

        this.searchCoursesByKeywordsTest("101", 2);
        this.searchCoursesByKeywordsTest("105", 2);
        this.searchCoursesByKeywordsTest("3", 2);

        // search across full title (combination of college, dept, etc.)
        this.searchCoursesByKeywordsTest("MET CS", MockData.NUM_MET_CS);
        this.searchCoursesByKeywordsTest("MET CS 102", 1);
        this.searchCoursesByKeywordsTest("CS course", MockData.NUMBER_OF_COURSES);
        this.searchCoursesByKeywordsTest(" number   104    MET  CS course   ", 1);
    }

    // helper method for "valid" course search testing
    void searchCoursesByKeywordsTest(String keywordsQuery, int expectedResultCount) {
        int expectedPageCount = CourseSearchAndSortService.calcPageCount(expectedResultCount);

        for (int page = 1; page <= expectedPageCount; page++) {
            var searchResultsMap = courseService.searchCoursesByKeywords(
                    keywordsQuery, page, SortMode.DEFAULT, Sort.Direction.DESC);
            var searchResults = (List<Course>)searchResultsMap.get("searchResults");

            int expectedResultCountPerPage = Math.min(
                    CourseSearchAndSortService.COURSES_PER_PAGE,
                    expectedResultCount - CourseSearchAndSortService.COURSES_PER_PAGE * (page - 1));
            assertEquals(expectedResultCountPerPage, searchResults.size());

            var keywords = Arrays.asList(StringUtils.split(keywordsQuery.toLowerCase(), " "));
            for (Course c : searchResults) {
                String fullTitle = c.getFullTitle().toLowerCase();
                keywords.forEach(keyword -> assertTrue(fullTitle.contains(keyword)));
            }
        }
    }

    // test course search with sorting
    @Test
    void searchCoursesByKeywordsWithSortingTest() {
        // test search and sort-by-rating
        var searchResultsMap = courseService.searchCoursesByKeywords(
                "MET CS", 1, SortMode.RATING, Sort.Direction.ASC);
        var searchResults = (List<Course>)searchResultsMap.get("searchResults");

        // check if correct number of search results returned
        assertEquals(MockData.NUM_MET_CS, searchResults.size());

        // check if each course has the specified keywords
        for (Course c : searchResults) {
            String fullTitle = c.getFullTitle().toLowerCase();
            assertTrue(fullTitle.contains("met"));
            assertTrue(fullTitle.contains("cs"));
        }

        // check for expected ordering
        Course prevC = null;
        for (Course c : searchResults) {
            assertEquals(1 + (c.getCourseNumber() % 5), c.getAverageRating());
            if (prevC != null) {
                // previous course rating must be <= current course
                assertTrue(prevC.getAverageRating() <= c.getAverageRating());
            }
            prevC = c;
        }
    }

    // test course search by keywords for "invalid" inputs
    @Test
    void searchCoursesByKeywordsInvalidTest() {
        var exception = ResponseStatusException.class;
        var sortMode = SortMode.DEFAULT;
        var sortDirection = Sort.Direction.DESC;

        assertThrows(exception, () -> courseService.searchCoursesByKeywords(
                "", 1, sortMode, sortDirection));
        assertThrows(exception, () -> courseService.searchCoursesByKeywords(
                "   ", 1, sortMode, sortDirection));

        // search for out-of-bounds page numbers (but using valid keyword)
        assertThrows(exception, () -> courseService.searchCoursesByKeywords(
                "MET", -3, sortMode, sortDirection));
        assertThrows(exception, () -> courseService.searchCoursesByKeywords(
                "MET", 0, sortMode, sortDirection));
        assertThrows(exception, () -> courseService.searchCoursesByKeywords(
                "MET", 99999, sortMode, sortDirection));
    }

    // test course search by keywords that do not produce any results
    @Test
    void searchCoursesByKeywordsNoResultsTest() {
        // search by gibberish text, then by invalid course number
        this.searchCoursesByKeywordsNoResultsTest("ikjk4 ilagio eklasd");
        this.searchCoursesByKeywordsNoResultsTest("999");

        // search course by MET and CAS college simultaneously --
        // when used individually, they produce results;
        // but when used together, they should not
        this.searchCoursesByKeywordsNoResultsTest("MET CAS");
    }

    void searchCoursesByKeywordsNoResultsTest(String keywords) {
        var searchResultsMap = courseService.searchCoursesByKeywords(
                keywords, 1, SortMode.DEFAULT, Sort.Direction.DESC);
        int courseCount = (int)searchResultsMap.get("courseCount");
        var searchResults = (List<Course>)searchResultsMap.get("searchResults");

        assertEquals(0, courseCount);
        assertEquals(0, searchResults.size());
    }
}
