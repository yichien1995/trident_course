package com.example.course.service.serviceimpl;

import com.example.course.data.dto.CourseDto;
import com.example.course.data.form.CourseForm;
import com.example.course.exception.ValidationException;
import com.example.course.repository.CourseRepository;
import com.example.course.repository.TeacherRepository;
import com.example.course.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final TeacherRepository teacherRepository;

    private final CourseRepository courseRepository;

    public CourseServiceImpl(TeacherRepository teacherRepository, CourseRepository courseRepository) {
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public List<CourseDto> getCourses() {
        return courseRepository.getCourses();
    }

    @Override
    public List<CourseDto> getCourseByTeacherId(long id) {
        boolean teacherExists = teacherRepository.checkTeacherExists(id);
        if (!teacherExists) {
            throw new ValidationException("The teacher doesn't exist.");
        }
        return courseRepository.findCourseByTeacherId(id);
    }

    @Override
    public void saveCourse(CourseForm form) {
        long teacherId = form.getTeacherId();
        boolean teacherExists = teacherRepository.checkTeacherExists(teacherId);
        if (!teacherExists) {
            throw new ValidationException("The teacher doesn't exist.");
        }
        courseRepository.saveCourseToDB(form);
    }

    @Override
    public void updateCourse(long id, CourseForm form) {
        boolean courseExists = courseRepository.checkCourseExists(id);
        if (!courseExists) {
            throw new ValidationException("The course with id:" + id + " doesn't exist.");
        }
        long teacherId = form.getTeacherId();
        boolean teacherExists = teacherRepository.checkTeacherExists(teacherId);
        if (!teacherExists) {
            throw new ValidationException("The teacher doesn't exist.");
        }
        courseRepository.updateCourseData(id, form);
    }

    @Override
    public void deleteCourse(long id) {
        boolean courseExists = courseRepository.checkCourseExists(id);
        if (!courseExists) {
            throw new ValidationException("The course with id:" + id + " doesn't exist.");
        }
        courseRepository.deleteCourseData(id);
    }
}
