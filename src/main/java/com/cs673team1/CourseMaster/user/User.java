package com.cs673team1.CourseMaster.user;

import com.cs673team1.CourseMaster.course.Course;
import com.cs673team1.CourseMaster.review.Review;
import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true)
    private String oAuth2Id;

    @NotBlank
    private String userName;

    @Size(max=100)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        ROLE_STUDENT, ROLE_ADMIN
    }

    @ManyToMany(mappedBy = "users")
    @JsonBackReference(value = "courses-user")
    private Set<Course> favoriteCourses;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Review> reviews;

    public User() {
    }

    public User(String oAuth2Id,
                @NotBlank String userName,
                @Size(max = 100) @NotBlank String email,
                Role role) {
        this.oAuth2Id = oAuth2Id;
        this.userName = userName;
        this.email = email;
        this.role = role;
    }

    // GETTERS AND SETTERS
    public Integer getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getoAuth2Id() {
        return oAuth2Id;
    }

    public void setoAuth2Id(String oAuth2Id) {
        this.oAuth2Id = oAuth2Id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<Course> getFavoriteCourses() {
        return favoriteCourses;
    }

    public void setFavoriteCourses(Set<Course> favoriteCourses) {
        this.favoriteCourses = favoriteCourses;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> review) {
        this.reviews = review;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", oAuth2Id='" + oAuth2Id + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
