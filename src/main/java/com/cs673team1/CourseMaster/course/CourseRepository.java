package com.cs673team1.CourseMaster.course;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Provides the repository for the course catalogue.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    /**
     * Searches the database for a list of courses with a course title similar
     * to the specified value.
     * @param title The title of the course to search for.
     * @return A list of courses with a course title similar to the specified
     *  value.
     */
    @Query(nativeQuery = true,
           value = "SELECT * FROM course WHERE upper(title) like "
                   + "concat('%', upper(?1), '%')")
    List<Course> findByTitle(String title);

    /**
     * Returns the entire course catalogue stored in the database.
     * @return The entire course catalogue from the database.
     */
    @EntityGraph(attributePaths = { "instructors", "users", "reviews" })
    List<Course> findAll();
}
