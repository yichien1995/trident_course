package com.example.course.repository;

import com.example.course.data.dto.CourseDto;
import com.example.course.data.form.CourseForm;

import java.util.List;

public interface CourseRepository {

    List<CourseDto> getCourses();

    List<CourseDto> findCourseByTeacherId(long id);

    void saveCourseToDB(CourseForm form);

    boolean checkCourseExists(long id);

    void updateCourseData(long id, CourseForm form);

    void deleteCourseData(long id);
}
