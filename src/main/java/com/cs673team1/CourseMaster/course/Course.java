package com.cs673team1.CourseMaster.course;

import com.cs673team1.CourseMaster.review.Review;
import com.cs673team1.CourseMaster.user.Instructor;
import com.cs673team1.CourseMaster.user.User;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Defines a course that can be reviewed by students.
 */
@Entity
public class Course {

    /**
     * Defines the type of course, whether a course is administered online,
     * in-person, or as hybrid/blended sessions.
     */
    public enum Type {
        /** Specifies that a course is administered online. */
        ONLINE,
        /** Specifies that a course is administered in-person. */
        IN_PERSON,
        /**
         * Specifies that a course is administered as a combination of online
         * and in-person sessions.
         */
        HYBRID;

        /**
         * Returns a friendly string representation of the enum instance.
         * @return A friendly string representation of this instance.
         */
        public String toFriendlyString() {
            switch (this) {
                case IN_PERSON: return "In Person";
                case ONLINE: return "Online";
                case HYBRID: return "Blended/eLive";
            }
            return "";
        }
    }

    /** Defines the minimum allowed course number. */
    private static final int MIN_COURSE_NUMBER = 100;
    /** Defines the maximum allowed course number. */
    private static final int MAX_COURSE_NUMBER = 999;

    /** Defines the minimum allowed course credits. */
    private static final int MIN_COURSE_CREDITS = 1;
    /** Defines the maximum allowed course credits. */
    private static final int MAX_COURSE_CREDITS = 8;

    /** Defines the minimum allowed course rating. */
    private static final int MIN_COURSE_RATING = 0;
    /** Defines the maximum allowed course rating. */
    private static final int MAX_COURSE_RATING = 5;

    /** Defines the maximum allowed course description. */
    private static final int MAX_COURSE_DESCRIPTION = 10000;


    /** Unique identifier for this course. */
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer id;

    /** The college where this course is taught. */
    @Enumerated(EnumType.STRING)
    private College college;

    /** The department classification for this course. */
    @NotBlank(message = "The course should have a department.")
    private String department;

    /** Three digit course number assigned by college or university. */
    @Range(min = MIN_COURSE_NUMBER, max = MAX_COURSE_NUMBER,
            message = "The course number should be between 100 and 999.")
    private Integer courseNumber;

    /** The title of this course. */
    @NotBlank(message = "The course should have a title.")
    private String title;

    /** Number of credit hours attributed to this course. */
    @Range(min = MIN_COURSE_CREDITS, max = MAX_COURSE_CREDITS,
            message = "The credit hours should be between 1 and 4.")
    private Integer credits;

    /**
     * The type of this course, whether it is administered online, in-person,
     * or as hybrid/blended sessions.
     */
    @Enumerated(EnumType.STRING)
    private Type type;

    /** Brief description of what is offered by this course. */
    @NotBlank(message = "The course should have a description.")
    @Column(length = MAX_COURSE_DESCRIPTION)
    private String description;

    /** A set of instructors that may teach this course. */
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "course_instructor",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "instructor_id"))
    private Set<Instructor> instructors;

    /** A set of users that favorite this course. */
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "course_user",
            joinColumns =  @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;

    /** A set of user reviews for this course. */
    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL)
    private Set<Review> reviews;

    /** The average rating of all reviews of this course. */
    @Range(min = MIN_COURSE_RATING, max = MAX_COURSE_RATING,
            message = "The average rating must be between 0 and 5.")
    @Column(columnDefinition = "float default 0.0")
    private Float averageRating;


    /**
     * Initializes a new Course instance.
     */
    public Course() {}

    /**
     * Initializes a new Course instance, using the specified parameters.
     * @param college The college where this course is taught.
     * @param department The department classification for this course.
     * @param courseNumber Three digit course number assigned by college or
     *                     university.
     * @param title The title of this course.
     * @param credits Number of credit hours attributed to this course.
     * @param type The type of this course.
     * @param description Brief description of what is offered by this course.
     * @param averageRating The average rating of all reviews of this course.
     */
    public Course(College college,
                  @NotBlank String department,
                  @Range(min = MIN_COURSE_NUMBER, max = MAX_COURSE_NUMBER)
                          Integer courseNumber,
                  @NotBlank String title,
                  @Range(min = MIN_COURSE_CREDITS, max = MAX_COURSE_CREDITS)
                          Integer credits,
                  Type type,
                  @NotBlank String description,
                  @Range(min = MIN_COURSE_RATING, max = MAX_COURSE_RATING)
                          Float averageRating) {
        this.college = college;
        this.department = department;
        this.courseNumber = courseNumber;
        this.title = title;
        this.credits = credits;
        this.type = type;
        this.description = description;
        this.averageRating = averageRating;
        this.instructors = new HashSet<>();
        this.users = new HashSet<>();
        this.reviews = new HashSet<>();
    }

    /**
     * Gets the unique identifier for this course.
     * @return The unique identifier for this course.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this course.
     * @param id The unique identifier for this course.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the college where this course is taught.
     * @return The college where this course is taught.
     */
    public College getCollege() {
        return college;
    }

    /**
     * Sets the college where this course is taught.
     * @param college The college where this course is taught.
     */
    public void setCollege(College college) {
        this.college = college;
    }

    /**
     * Gets the department classification for this course.
     * @return The department classification for this course.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department classification for this course.
     * @param department The department classification for this course.
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Gets the three digit course number assigned by college or university.
     * @return Three digit course number assigned by college or university.
     */
    public Integer getCourseNumber() {
        return courseNumber;
    }

    /**
     * Sets the three digit course number assigned by college or university.
     * @param courseNumber Three digit course number assigned by college or
     *                     university.
     */
    public void setCourseNumber(Integer courseNumber) {
        this.courseNumber = courseNumber;
    }

    /**
     * Gets the title of this course.
     * @return The title of this course.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this course.
     * @param title The title of this course.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the number of credit hours attributed to this course.
     * @return Number of credit hours attributed to this course.
     */
    public Integer getCredits() {
        return credits;
    }

    /**
     * Sets the number of credit hours attributed to this course.
     * @param credits Number of credit hours attributed to this course.
     */
    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    /**
     * Gets the type of this course, whether it is administered online,
     * in-person, or as hybrid/blended sessions.
     * @return The type of this course.
     */
    public Type getType() {
        return type;
    }

    /**
     * Gets the type of this course, whether it is administered online,
     * in-person, or as hybrid/blended sessions.
     * @param type The type of this course.
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Gets the brief description of what is offered by this course.
     * @return Brief description of what is offered by this course.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the brief description of what is offered by this course.
     * @param description Brief description of what is offered by this course.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets a set of instructors that may teach this course.
     * @return Set of instructors that may teach this course.
     */
    public Set<Instructor> getInstructors() {
        return instructors;
    }

    /**
     * Sets a set of instructors that may teach this course.
     * @param instructor Set of instructors that may teach this course.
     */
    public void setInstructors(Set<Instructor> instructor) {
        this.instructors = instructor;
    }

    /**
     * Gets a set of users that favorite this course.
     * @return Set of users that favorite this course.
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * Sets a set of users that favorite this course.
     * @param users Set of users that favorite this course.
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }

    /**
     * Gets a list of user reviews for this course.
     * @return List of user reviews for this course.
     */
    public List<Review> getReviews() {
        var sortedReviews = new ArrayList<>(reviews);
        sortedReviews.sort(Comparator.comparing(Review::getCourseDate).reversed());
        return sortedReviews;
    }

    /**
     * Sets a set of user reviews for this course.
     * @param review Set of user reviews for this course.
     */
    public void setReviews(Set<Review> review) {
        this.reviews = review;
    }

    /**
     * Gets the average rating of all reviews of this course.
     * @return The average rating of all reviews of this course.
     */
    public float getAverageRating() {
        return averageRating;
    }

    /**
     * Sets the average rating of all reviews of this course.
     * @param averageRating The average rating of all reviews of this course.
     */
    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    /**
     * Returns the total number of reviews for this course.
     * @return The total number of reviews for this course.
     */
    public int getReviewCount() {
        return reviews.size();
    }

    /**
     * Returns the full title of the course, including the course's college,
     * department, course number, and title, in that order. An example of the
     * full title is as follows:  MET CS 101 Intro to Computer Science.
     * @return The full title of this course.
     */
    public String getFullTitle() {
        return this.college + " " + this.department + " " + this.courseNumber
                + " - " + this.title +
                (this.type == Type.IN_PERSON ? ""
                    : " (" + this.type.toFriendlyString() + ")");
    }

    /**
     * Returns a structured string representation of this course.
     * @return A string representation of the course.
     */
    @Override
    public String toString() {
        return "Course{"
                + "ID=" + id
                + ", college='" + college + '\''
                + ", department='" + department + '\''
                + ", courseNumber=" + courseNumber
                + ", title='" + title + '\''
                + ", credits=" + credits
                + ", type=" + type
                + ", description='" + description + '\''
                + ", averageRating='" + averageRating + '\''
                + '}';
    }
}
