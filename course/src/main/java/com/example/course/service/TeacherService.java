package com.example.course.service;

import com.example.course.data.dto.TeacherDto;
import com.example.course.data.form.TeacherForm;
import com.example.course.exception.UserNotFoundException;

import java.util.List;

public interface TeacherService {
    void saveTeacher(TeacherForm form);

    List<TeacherDto> getTeachers();
}
