package com.example.course.data.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TeacherForm {

    @NotBlank(message = "Email cannot be null or empty")
    @JsonProperty("email")
    private String email;


    @NotBlank(message = "Name cannot be null or empty")
    @JsonProperty("name")
    private String name;


    @NotBlank(message = "Department cannot be null or empty")
    @JsonProperty("department")
    private String department;
}
