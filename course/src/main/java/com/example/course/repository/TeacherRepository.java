package com.example.course.repository;

import com.example.course.data.dto.TeacherDto;
import com.example.course.data.form.TeacherForm;

import java.util.List;

public interface TeacherRepository {
    void saveTeacherToDB(TeacherForm form, long userId);
    boolean checkTeacherExists(long userId);

    List<TeacherDto> findAllTeachers();
}
