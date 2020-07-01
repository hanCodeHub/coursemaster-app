package com.cs673team1.CourseMaster.course;

import com.cs673team1.CourseMaster.review.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;
import java.util.List;


@Service
public class MockData {

    static final int NUM_MET_CS = 6;
    static final int NUM_CAS_CS = 6;
    static final int NUMBER_OF_COURSES = NUM_MET_CS + NUM_CAS_CS;
    static final int NUMBER_OF_COURSE_PAGES = 2;

    final List<Course> testCourses = new ArrayList<>();

    @Autowired
    private CourseRepository courseRepository;

    MockData() {
        // add-in mock course-data for "MET CS"
        for (int i = 0; i < NUM_MET_CS; i++) {
            this.testCourses.add(this.createCourseWithReview(College.MET, 101 + i));
        }

        // add-in mock course-data for "CAS CS"
        for (int i = 0; i < NUM_CAS_CS; i++) {
            this.testCourses.add(createCourseWithReview(College.CAS, 101 + i));
        }
    }

    void setUp() {
        this.tearDown(); // delete previous/initial data
        this.courseRepository.saveAll(testCourses);

        // obtain auto-updated uid's from repo
        var repoCourses = courseRepository.findAll();
        for (int i = 0; i < repoCourses.size(); i++) {
            testCourses.get(i).setId(repoCourses.get(i).getId());
        }
    }

    void tearDown() {
        this.courseRepository.deleteAll();
    }

    // helper for creating a course with review
    public Course createCourseWithReview(College college, int courseNumber) {
        float rating =  1 + (courseNumber % 5F);
        Course course = new Course(
                college, "CS", courseNumber, "Course number " + courseNumber,
                4, Course.Type.IN_PERSON, "This is course " + courseNumber,
                rating);
        Review review = new Review(
                "course " + courseNumber + " fake review", rating);
        review.setCourse(course);
        course.setReviews(Set.of(review));
        return course;
    }

}
