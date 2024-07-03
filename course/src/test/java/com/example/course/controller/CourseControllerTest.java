package com.example.course.controller;

import com.example.course.data.ResponseObject;
import com.example.course.data.dto.CourseDto;
import com.example.course.data.dto.TeacherDto;
import com.example.course.data.form.CourseForm;
import com.example.course.exception.ValidationException;
import com.example.course.service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void get_courses() throws Exception {
        TeacherDto teacherInfo = new TeacherDto(2L, "b@gmail.com", "Bob", "MATH");
        CourseDto calculusCourse = new CourseDto(1L, "Calculus",
                "An introductory course to Calculus covering limits, derivatives, and integrals.",
                "201", "Monday", LocalTime.of(9, 0), LocalTime.of(10, 0), teacherInfo);
        CourseDto linearAlgebraCourse = new CourseDto(2L, "Linear Algebra",
                "A fundamental course in Linear Algebra focusing on vector spaces, linear transformations, and matrix theory.",
                "111", "Friday", LocalTime.of(14, 0), LocalTime.of(15, 0), teacherInfo);

        List<CourseDto> courseList = List.of(calculusCourse, linearAlgebraCourse);

        when(courseService.getCourses()).thenReturn(courseList);

        mockMvc.perform(get("/api/1.0/courses")
                        .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(new ResponseObject<>(courseList))));
    }

    @Test
    public void create_course_success() throws Exception {
        CourseForm form = new CourseForm("Geometry",
                "An in-depth study of Geometry covering points, lines, angles, and shapes.",
                "301", "Monday", LocalTime.of(13, 0), LocalTime.of(15, 0), 1L);

        mockMvc.perform(post("/api/1.0/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Geometry\",\"introduction\":\"An in-depth study of Geometry covering points, lines, angles, and shapes.\"," +
                                "\"classroom\":\"301\",\"day\":\"Monday\",\"start_time\":\"15:00\",\"end_time\":\"16:30\",\"teacher_id\":1}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Create a new course successfully."));
    }

    @Test
    public void create_course_bad_request() throws Exception {
        CourseForm form = new CourseForm("Geometry",
                "An in-depth study of Geometry covering points, lines, angles, and shapes.",
                "301", "Monday", LocalTime.of(13, 0), LocalTime.of(15, 0), 1);

        // Mocking service to throw a ValidationException
        doThrow(new ValidationException("The teacher doesn't exist.")).when(courseService).saveCourse(any(CourseForm.class));

        mockMvc.perform(post("/api/1.0/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Geometry\",\"introduction\":\"An in-depth study of Geometry covering points, lines, angles, and shapes.\"," +
                                "\"classroom\":\"301\",\"day\":\"Monday\",\"start_time\":\"15:00\",\"end_time\":\"16:30\",\"teacher_id\":1}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("The teacher doesn't exist."));
    }

    @Test
    public void delete_course_success() throws Exception {
        long courseId = 1L;

        mockMvc.perform(delete("/api/1.0/course/{id}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("Delete course(id:" + courseId + ") successfully."));
    }

    @Test
    public void delete_course_not_found() throws Exception {
        long courseId = 999L;

        // Mocking service to throw a ValidationException
        doThrow(new ValidationException("Course not found.")).when(courseService).deleteCourse(courseId);

        mockMvc.perform(delete("/api/1.0/course/{id}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Course not found."));
    }
}

