package com.cs673team1.CourseMaster.review;

import com.cs673team1.CourseMaster.course.Course;
import com.cs673team1.CourseMaster.user.Instructor;
import com.cs673team1.CourseMaster.user.User;
import com.fasterxml.jackson.annotation.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = 0;

    // review date. change property name in future release.
    private LocalDate courseDate;

    @Column(length = 10000)
    private String comment;

    @Range(min = 1, max = 5, message = "Score should between 1 and 5")
    private Float rating;

    @ManyToOne(fetch = FetchType.EAGER)
    private Instructor instructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "user-review")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "course-review")
    private Course course;

    /**
     *
     * @return review id.
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id set review id.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return get the data from course.
     */
    public LocalDate getCourseDate() {
        return courseDate;
    }

    /**
     *
     * @param studyYear set course study year into course data.
     */
    public void setCourseDate(LocalDate studyYear) {
        this.courseDate = studyYear;
    }

    /**
     *
     * @return get the instructor of the course.
     */
    public Instructor getInstructor() {
        return instructor;
    }

    /**
     *
     * @param instructor set the instructor of course.
     */
    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    /**
     *
     * @return get the comment of course.
     */
    public String getComment() {
        return comment;
    }

    /**
     *
     * @param comment set the comment of course
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     *
     * @return get the rating of course
     */
    public Float getRating() {
        return rating;
    }

    /**
     *
     * @param rating the rating of course
     */
    public void setRating(Float rating) {
        this.rating = rating;
    }

    /**
     *
     * @return get the user of review
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param user review owner.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     * @return course of review
     */
    public Course getCourse() {
        return course;
    }

    /**
     *
     * @param course comments that belong to which course
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     *
     * @return review user name
     */
    public String getUserName() {
        // NOTE: This method is not used on the backend, however, it is
        //       required by the frontend, due to JsonBackReference and
        //       serialization issues.
        return (this.user == null) ? "" : this.user.getUserName();
    }

    public Review() {
    }

    public Review(String comment, float rating) {
        this.comment = comment;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", user=" + user +
                ", studyYear=" + courseDate +
                ", professor='" + instructor + '\'' +
                ", comment='" + comment + '\'' +
                ", score=" + rating +
                '}';
    }
}
