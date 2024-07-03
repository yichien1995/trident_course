package com.example.course.controller;

import com.example.course.data.ResponseObject;
import com.example.course.data.dto.CourseDto;
import com.example.course.data.dto.TeacherDto;
import com.example.course.data.form.TeacherForm;
import com.example.course.exception.UserNotFoundException;
import com.example.course.exception.ValidationException;
import com.example.course.response.ErrorResponse;
import com.example.course.response.SuccessResponse;
import com.example.course.service.CourseService;
import com.example.course.service.TeacherService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/1.0")
public class TeacherController {

    private final TeacherService teacherService;
    private final CourseService courseService;

    public TeacherController(TeacherService teacherService, CourseService courseService) {
        this.teacherService = teacherService;
        this.courseService = courseService;
    }

    @Operation(summary = "Get all teachers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved teachers information.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "data": [
                                        {
                                          "id": 1,
                                          "email": "a@gmail.com",
                                          "name": "Alice",
                                          "department": "CS"
                                        },
                                        {
                                          "id": 2,
                                          "email": "b@gmail.com",
                                          "name": "Bob",
                                          "department": "MATH"
                                        }
                                      ]
                                    }                                                                     
                                    """
                            )
                    ))
    })
    @GetMapping("/teachers")
    public ResponseEntity<?> getTeachers() {
        List<TeacherDto> teachers = teacherService.getTeachers();
        return ResponseEntity.ok().body(new ResponseObject<>(teachers));
    }

    @Operation(summary = "Get courses by teacher ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved courses by teacher id.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "data": [
                                        {
                                          "id": 4,
                                          "course_name": "Introduction to Algorithms",
                                          "introduction": "A course that introduces fundamental algorithms, their design, analysis, and implementation.",
                                          "classroom": "201",
                                          "day": "Thursday",
                                          "start_time": "11:00:00",
                                          "end_time": "12:00:00",
                                          "teacher_info": {
                                            "id": 1,
                                            "email": "a@gmail.com",
                                            "name": "alice",
                                            "department": "CS"
                                          }
                                        },
                                        {
                                          "id": 5,
                                          "course_name": "Introduction to Algorithms",
                                          "introduction": "A course that introduces fundamental algorithms, their design, analysis, and implementation.",
                                          "classroom": "201",
                                          "day": "Thursday",
                                          "start_time": "11:00:00",
                                          "end_time": "12:00:00",
                                          "teacher_info": {
                                            "id": 1,
                                            "email": "a@gmail.com",
                                            "name": "alice",
                                            "department": "CS"
                                          }
                                        }
                                      ]
                                    }                      
                                    """))),
            @ApiResponse(responseCode = "404", description = "Teacher not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": "The teacher doesn't exist."
                                    }
                                    """)))
    })
    @GetMapping("/teacher/{id}/course")
    public ResponseEntity<?> getCourseByTeacherId(@PathVariable long id) {
        try {
            List<CourseDto> courses = courseService.getCourseByTeacherId(id);
            return ResponseEntity.ok().body(new ResponseObject<>(courses));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        }

    }

    @Operation(summary = "Create a new teacher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teacher created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "msg": "Create new teacher successfully."
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": "The teacher already exists."
                                    }
                                     """)))
    })
    @PostMapping(path = "/teacher", consumes = {"application/json"})
    public ResponseEntity<?> createTeacher(@Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TeacherForm.class),
                    examples = @ExampleObject(value = """
                            {
                              "email": "d@gmail.com",
                              "name": "David",
                              "department": "CS"
                            }
                            """)))
                                           TeacherForm form,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> {
                errorMessage.append(error.getDefaultMessage()).append("; ");
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errorMessage.toString()));
        }

        try {
            teacherService.saveTeacher(form);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Create new teacher successfully."));
        } catch (UserNotFoundException | ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }
}
