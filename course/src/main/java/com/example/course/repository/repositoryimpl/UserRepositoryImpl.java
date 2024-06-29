package com.example.course.repository.repositoryimpl;

import com.example.course.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveUserToDB(String email, String password){
        String sql = "INSERT INTO user(email, password) VALUES (?,?)";
        jdbcTemplate.update(sql,email,password);
    }
    @Override
    public boolean checkEmailExists(String email) {
        String sql = "SELECT COUNT(email) FROM user WHERE email = ?";
        int result = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return result == 1;
    }

    @Override
    public long getUserIdByEmail(String email) {
        String sql = "SELECT id FROM user WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, Long.class,email);
    }
}
