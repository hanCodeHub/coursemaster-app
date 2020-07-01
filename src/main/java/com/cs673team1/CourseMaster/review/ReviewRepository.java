package com.cs673team1.CourseMaster.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>  {

    @Query(nativeQuery = true, value = "SELECT * FROM review WHERE review_course_Id = ?1")
    List<Review> findByID(Integer id);

    // find reviews by course ID
    @Query(nativeQuery = true, value =
            "SELECT review_Course_Id, study_Year, professor , comment, score  " +
                    "FROM course " +
                    "INNER JOIN review " +
                    "ON course_Id = review_course_Id " +
                    "WHERE course_Id = ?1")
    List<Map<String, Object>> findReviewById(Integer id);

    // find reviews by user id
    @Query(value = "SELECT * FROM review WHERE user_id = ?1", nativeQuery = true)
    Set<Review> findByUserId(Integer userId);
}
