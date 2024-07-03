package com.example.course.repository.repositoryimpl;

import com.example.course.data.dto.TeacherDto;
import com.example.course.data.form.TeacherForm;
import com.example.course.repository.TeacherRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TeacherRepositoryImpl implements TeacherRepository {

    private final JdbcTemplate jdbcTemplate;

    public TeacherRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveTeacherToDB(TeacherForm form, long userId) {
        String sql = "INSERT INTO teacher(name, department, user_id) VALUES (?,?,?)";
        jdbcTemplate.update(sql, form.getName(), form.getDepartment(), userId);
    }

    @Override
    public boolean checkTeacherExistsByUserId(long userId) {
        String sql = "SELECT COUNT(id) FROM teacher WHERE user_id = ?";
        int result = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return result == 1;
    }

    @Override
    public boolean checkTeacherExists(long id) {
        String sql = "SELECT COUNT(id) FROM teacher WHERE id = ?";
        int result = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return result == 1;
    }

    @Override
    public List<TeacherDto> getAllTeachers() {
        String sql = "SELECT t.id, u.email, t.name, t.department FROM teacher AS t JOIN user AS u ON t.user_id = u.id;";
        RowMapper<TeacherDto> mapper = getTeacherDtoRowMapper();
        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public TeacherDto findTeacherById(long id) {
        String sql = "SELECT t.id, u.email, t.name, t.department FROM teacher AS t JOIN user AS u ON t.user_id = u.id WHERE t.id = ?;";
        RowMapper<TeacherDto> mapper = getTeacherDtoRowMapper();
        return jdbcTemplate.queryForObject(sql, mapper, id);
    }

    private RowMapper<TeacherDto> getTeacherDtoRowMapper() {
        return new RowMapper<TeacherDto>() {
            @Override
            public TeacherDto mapRow(ResultSet rs, int rowNum) throws SQLException {

                TeacherDto dto = new TeacherDto();
                dto.setId(rs.getLong("id"));
                dto.setEmail(rs.getString("email"));
                dto.setName(rs.getString("name"));
                dto.setDepartment(rs.getString("department"));
                return dto;
            }
        };
    }
}
