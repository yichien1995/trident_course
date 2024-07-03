package com.example.course.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {

    @JsonProperty("id")
    private long id;

    @JsonProperty("course_name")
    private String name;

    @JsonProperty("introduction")
    private String introduction;

    @JsonProperty("classroom")
    private String classroom;

    @JsonProperty("day")
    private String day;

    @JsonProperty("start_time")
    private LocalTime startTime;

    @JsonProperty("end_time")
    private LocalTime endTime;

    @JsonProperty("teacher_info")
    private TeacherDto teacherInfo;
}
