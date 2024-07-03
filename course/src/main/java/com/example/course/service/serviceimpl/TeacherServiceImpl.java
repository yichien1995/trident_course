package com.example.course.service.serviceimpl;

import com.example.course.data.dto.TeacherDto;
import com.example.course.data.form.TeacherForm;
import com.example.course.exception.UserNotFoundException;
import com.example.course.exception.ValidationException;
import com.example.course.repository.TeacherRepository;
import com.example.course.repository.UserRepository;
import com.example.course.service.TeacherService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final UserRepository userRepository;

    private final TeacherRepository teacherRepository;

    public TeacherServiceImpl(UserRepository userRepository,
                              TeacherRepository teacherRepository) {
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public void saveTeacher(TeacherForm form) {
        String email = form.getEmail();
        boolean emailExists = userRepository.checkEmailExists(email);
        if (!emailExists) {
            throw new UserNotFoundException("User doesn't exists.");
        }
        long userId = userRepository.getUserIdByEmail(email);
        boolean teacherExists = teacherRepository.checkTeacherExistsByUserId(userId);
        if (teacherExists) {
            throw new ValidationException("The teacher already exists.");
        }
        teacherRepository.saveTeacherToDB(form, userId);
    }

    @Override
    public List<TeacherDto> getTeachers() {
        return teacherRepository.getAllTeachers();
    }
}
