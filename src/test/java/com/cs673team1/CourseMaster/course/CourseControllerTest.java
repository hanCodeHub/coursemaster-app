package com.cs673team1.CourseMaster.course;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockData courseMockData;

    @BeforeEach
    void setUp() {
        courseMockData.setUp();
    }

    @AfterEach
    void tearDown() {
        courseMockData.tearDown();
    }


    @Test
    @WithMockUser(value = "test user")
    void retrieveCourseCount() throws Exception {
        this.mockMvc.perform(get("/api/courses/count")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "test user")
    void retrieveCoursePageCount() throws Exception {
        this.mockMvc.perform(get("/api/courses/pages/count")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "test user")
    void retrieveAllCourses() throws Exception {
        this.mockMvc.perform(get("/api/courses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "test user")
    void retrieveCoursesByPage() throws Exception {
        this.mockMvc.perform(get("/api/courses/pages/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // invalid pages
        this.mockMvc.perform(get("/api/courses/pages/-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/api/courses/pages/99999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // with sorting
        this.mockMvc.perform(get("/api/courses/pages/1")
                .param("sort", "RATING")
                .param("direction", "ASC")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/api/courses/pages/1")
                .param("sort", "REVIEW_COUNT")
                .param("direction", "DESC")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "test user")
    void retrieveCourseById() throws Exception {
        int mockId = courseMockData.testCourses.get(0).getId();
        this.mockMvc.perform(get("/api/courses/" + mockId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/api/courses/99999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(value = "test user")
    void retrieveCourseByTitle() throws Exception {
        this.mockMvc.perform(get("/api/courses/title=Course")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        this.mockMvc.perform(get("/api/courses/title=dkad3sf37f45oj3oiz56vwa'")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(value = "test user")
    void searchCoursesByKeywords() throws Exception {
        // valid keywords/pages
        this.mockMvc.perform(get("/api/courses/search")
                .param("keywords", "course")
                .param("page", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/api/courses/search")
                .param("keywords", "course")
                .param("page", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // valid searching with sorting
        this.mockMvc.perform(get("/api/courses/search")
                .param("keywords", "course")
                .param("page", "2")
                .param("sort", "REVIEW_COUNT")
                .param("direction", "ASC")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/api/courses/search")
                .param("keywords", "course")
                .param("page", "2")
                .param("sort", "RATING")
                .param("direction", "DESC")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // invalid pages
        this.mockMvc.perform(get("/api/courses/search")
                .param("keywords", "course")
                .param("page", "-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/api/courses/search")
                .param("keywords", "course")
                .param("page", "99999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // invalid keywords
        this.mockMvc.perform(get("/api/courses/search")
                .param("keywords", "")
                .param("page", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(value = "test user")
    void retrieveCourseReviewById() throws Exception {
        int mockId = courseMockData.testCourses.get(0).getId();
        this.mockMvc.perform(get("/api/courses/" + mockId + "/reviews")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
