package com.example.course.data.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUpForm {

    @NotBlank(message = "Email cannot be null or empty")
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "Password cannot be null or empty")
    @JsonProperty("password")
    private String password;
}
