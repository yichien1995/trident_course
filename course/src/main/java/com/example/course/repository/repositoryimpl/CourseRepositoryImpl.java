package com.example.course.repository.repositoryimpl;

import com.example.course.data.dto.CourseDto;
import com.example.course.data.form.CourseForm;
import com.example.course.repository.CourseRepository;
import com.example.course.repository.TeacherRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CourseRepositoryImpl implements CourseRepository {

    private final JdbcTemplate jdbcTemplate;

    private final TeacherRepository teacherRepository;

    public CourseRepositoryImpl(JdbcTemplate jdbcTemplate,
                                TeacherRepository teacherRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public List<CourseDto> getCourses() {
        String sql = "SELECT * FROM course";
        RowMapper<CourseDto> mapper = getCourseDtoRowMapper();
        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public List<CourseDto> findCourseByTeacherId(long id) {
        String sql = "SELECT * FROM course WHERE teacher_id = ?";
        RowMapper<CourseDto> mapper = getCourseDtoRowMapper();
        return jdbcTemplate.query(sql, mapper, id);
    }

    @Override
    public void saveCourseToDB(CourseForm form) {
        String sql = "INSERT INTO course(name, introduction, classroom, day, start_time, end_time, teacher_id) " +
                "VALUES (?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, form.getName(), form.getIntroduction(), form.getClassroom(), form.getDay(),
                form.getStartTime(), form.getEndTime(), form.getTeacherId());
    }

    @Override
    public boolean checkCourseExists(long id) {
        String sql = "SELECT COUNT(id) FROM course WHERE id = ?";
        int result = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return result == 1;
    }

    @Override
    public void updateCourseData(long id, CourseForm form) {
        String sql = "UPDATE course SET name = ?, introduction = ?, classroom = ?, day = ?, " +
                "start_time = ?, end_time = ?, teacher_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, form.getName(), form.getIntroduction(), form.getClassroom(), form.getDay(),
                form.getStartTime(), form.getEndTime(), form.getTeacherId(), id);
    }

    @Override
    public void deleteCourseData(long id) {
        String sql = "DELETE FROM course WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private RowMapper<CourseDto> getCourseDtoRowMapper() {
        return new RowMapper<CourseDto>() {
            @Override
            public CourseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                CourseDto dto = new CourseDto();
                dto.setId(rs.getLong("id"));
                dto.setName(rs.getString("name"));
                dto.setIntroduction(rs.getString("introduction"));
                dto.setClassroom(rs.getString("classroom"));
                dto.setDay(rs.getString("day"));
                dto.setStartTime(rs.getTime("start_time").toLocalTime());
                dto.setEndTime(rs.getTime("end_time").toLocalTime());
                dto.setTeacherInfo(teacherRepository.findTeacherById(rs.getLong("teacher_id")));
                return dto;
            }
        };
    }
}
