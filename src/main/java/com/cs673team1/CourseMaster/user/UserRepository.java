package com.cs673team1.CourseMaster.user;

import com.cs673team1.CourseMaster.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class for database access. Type parameters: Entity, Unique Identifier
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     *
     * @param email User email address used for search
     * @return Returns User if found
     */
    @Query(value =
            "SELECT * FROM user WHERE email = ?1", nativeQuery = true)
    User findByEmail(String email);

    /**
     *
     * @param id User's oauth2id
     * @return Returns User if found
     */
    @Query(value =
            "SELECT * FROM user WHERE oauth2id = ?1", nativeQuery = true)
    User findByOAuth2Id(String id);

    /**
     *
     * @param id User's unique id
     * @return Returns a list of user's favorite courses
     */
    @Query(nativeQuery = true, value =
            "SELECT favoriteCourses from user WHERE user_id = ?1")
    List<Course> findFavoriteCourses(String id);
}
