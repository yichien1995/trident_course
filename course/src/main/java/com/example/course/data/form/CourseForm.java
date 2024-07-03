package com.example.course.data.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class CourseForm {

    @NotBlank(message = "Name cannot be null or empty.")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "Introduction cannot be null or empty.")
    @JsonProperty("introduction")
    private String introduction;

    @NotBlank(message = "Classroom cannot be null or empty.")
    @JsonProperty("classroom")
    private String classroom;

    @NotBlank(message = "Day cannot be null or empty.")
    @JsonProperty("day")
    private String day;

    @NotNull(message = "Start time cannot be null.")
    @JsonProperty("start_time")
    private LocalTime startTime;

    @NotNull(message = "End time cannot be null.")
    @JsonProperty("end_time")
    private LocalTime endTime;

    @NotNull(message = "Teacher id cannot be null.")
    @JsonProperty("teacher_id")
    private long teacherId;
}
