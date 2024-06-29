package com.example.course.repository;

public interface UserRepository {
    void saveUserToDB(String email, String password);
    boolean checkEmailExists(String email);

    long getUserIdByEmail(String email);
}
