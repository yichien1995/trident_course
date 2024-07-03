package com.example.course.service;

import com.example.course.data.dto.CourseDto;
import com.example.course.data.form.CourseForm;

import java.util.List;

public interface CourseService {

    List<CourseDto> getCourses();

    List<CourseDto> getCourseByTeacherId(long id);

    void saveCourse(CourseForm form);

    void updateCourse(long id, CourseForm form);

    void deleteCourse(long id);
}
