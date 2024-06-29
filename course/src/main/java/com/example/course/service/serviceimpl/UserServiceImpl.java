package com.example.course.service.serviceimpl;

import com.example.course.data.form.SignUpForm;
import com.example.course.exception.ValidationException;
import com.example.course.repository.UserRepository;
import com.example.course.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(SignUpForm form) {
        String email = form.getEmail();
        boolean checkEmailRegex = emailValidator(email);
        boolean checkEmailExists = emailExists(email);
        if (!checkEmailRegex){
            throw new ValidationException("Invalid email format");
        }

        if (checkEmailExists){
            throw new ValidationException("Email already exists");
        }

        String encodePassword = passwordEncoder.encode(form.getPassword());
        userRepository.saveUserToDB(form.getEmail(),encodePassword);
    }

    private boolean emailValidator(String email) {
        String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean emailExists(String email) {
        return userRepository.checkEmailExists(email);
    }
}
