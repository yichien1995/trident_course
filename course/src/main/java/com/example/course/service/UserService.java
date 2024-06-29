package com.example.course.service;

import com.example.course.data.form.SignUpForm;
import org.springframework.stereotype.Service;


public interface UserService {
    void saveUser(SignUpForm form);
}
