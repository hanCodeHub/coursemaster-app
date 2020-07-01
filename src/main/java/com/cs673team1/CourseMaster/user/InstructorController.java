package com.cs673team1.CourseMaster.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("api/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    @Autowired
    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    /**
     *
     * @param id the id of the course
     * @return the set of instructors teach this course.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Set<Instructor> > retrieveInstructorByCourseID(
            @PathVariable Integer id) {
        var instructor = instructorService.getInstructorsByCourseId(id);
        return new ResponseEntity<>(instructor, HttpStatus.OK);
    }
}
