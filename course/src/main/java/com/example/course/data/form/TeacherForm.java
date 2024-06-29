package com.example.course.data.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

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
