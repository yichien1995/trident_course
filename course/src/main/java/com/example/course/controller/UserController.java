package com.example.course.controller;

import com.example.course.data.form.SignUpForm;
import com.example.course.exception.ValidationException;
import com.example.course.response.ErrorResponse;
import com.example.course.response.SuccessResponse;
import com.example.course.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/1.0/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Sign up")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully signed up",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "msg": "Sign up successfully."
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": "Email already exists"
                                    }
                                    """)))
    })
    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<?> signUp(@Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SignUpForm.class),
                    examples = @ExampleObject(value = """
                            {
                              "email": "test@gmail.com",
                              "password": "password"
                            }
                            """)))
                                    SignUpForm form,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> {
                errorMessage.append(error.getDefaultMessage()).append("; ");
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errorMessage.toString()));
        }

        try {
            userService.saveUser(form);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Sign up successfully."));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }
}
