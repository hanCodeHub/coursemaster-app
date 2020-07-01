package com.cs673team1.CourseMaster.user;

import com.cs673team1.CourseMaster.course.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class InstructorService {

    private final CourseService courseService;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    public InstructorService(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     *
     * @param courseId the id of the course
     * @return the set of instructors teach this course.
     */
    public Set<Instructor> getInstructorsByCourseId(Integer courseId) {
        var course = courseService.getCourseByID(courseId);
        return course.getInstructors();
    }
}
