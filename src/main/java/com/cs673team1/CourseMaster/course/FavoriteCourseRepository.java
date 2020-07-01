
package com.cs673team1.CourseMaster.course;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoriteCourseRepository extends JpaRepository<Course, Integer> {
    @Query(nativeQuery = true,
            value = "SELECT \n" +
                    "c.*, popular\n" +
                    "FROM COURSE c\n" +
                    "JOIN (\n" +
                    "\tSELECT COURSE_ID, COUNT(COURSE_ID) as popular\n" +
                    "        FROM COURSE_USER GROUP BY COURSE_ID ORDER BY popular DESC\n" +
                    "        LIMIT ?1" +
                    ") as r ON r.COURSE_ID = c.ID  \n" +
                    "GROUP BY c.id\n" +
                    "ORDER BY popular DESC")
    List<Course> listFavoriteCourses(int count);
}
