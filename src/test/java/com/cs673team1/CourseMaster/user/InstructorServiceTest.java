package com.cs673team1.CourseMaster.user;

import com.cs673team1.CourseMaster.course.College;
import com.cs673team1.CourseMaster.course.Course;
import com.cs673team1.CourseMaster.course.CourseRepository;
import com.cs673team1.CourseMaster.course.CourseService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InstructorServiceTest {

    @Autowired
    private InstructorService instructorService;

    @Autowired
    private CourseRepository courseRepository;

    private Integer courseId;

    private Set<Instructor> instructors1;

    private static final String UNIQUE_COURSE_NAME = "__-Unknown@#$Course*()>?";

    @BeforeEach
    void setUp() {
        // create fake instructor data
        Instructor instructor1 = new Instructor();
        Instructor instructor2 = new Instructor();
        Instructor instructor3 = new Instructor();
        Instructor instructor4 = new Instructor();
        instructor1.setName("Fake_name1");
        instructor2.setName("Fake_name2");
        instructor3.setName("Fake_name3");
        instructor4.setName("Fake_name4");
        instructors1 = Set.of(instructor1,instructor2,instructor3);

        // create fake course data
        var course1 = new Course(College.MET, "CS", 333, UNIQUE_COURSE_NAME,
                4, Course.Type.ONLINE, "test_description", 0F);
        List<Course> courses = new ArrayList<>();
        courses.add(course1);

        // build many yo many relationship
        instructors1.forEach(r->r.setCourses(courses));
        course1.setInstructors(instructors1);
        for (Course course : courses) {
            courseRepository.save(course);
        }

        courseId = courseRepository.findByTitle(UNIQUE_COURSE_NAME).get(0).getId();
    }

    @AfterEach
    void tearDown() {
        courseRepository.deleteById(courseId);
        instructors1 = null;
    }

    @Test
    void TestGetInstructors() {
        var courseInstructors = instructorService.getInstructorsByCourseId(courseId);

        // test instructors size
        assertEquals(courseInstructors.size(), instructors1.size());

        // tests for reviews objects
        List<String> instructorsName =
                instructors1.stream().map(Instructor::getName).collect(Collectors.toList());

        courseInstructors.stream().map(Instructor::getName).forEach(e -> assertTrue(instructorsName.contains(e)));
    }
}
