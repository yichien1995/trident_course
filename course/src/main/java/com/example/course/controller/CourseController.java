package com.example.course.controller;

import com.example.course.data.ResponseObject;
import com.example.course.data.dto.CourseDto;
import com.example.course.data.form.CourseForm;
import com.example.course.exception.ValidationException;
import com.example.course.response.ErrorResponse;
import com.example.course.response.SuccessResponse;
import com.example.course.service.CourseService;
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
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Operation(summary = "Get all courses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved courses",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseObject.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "data": [
                                        {
                                          "id": 1,
                                          "course_name": "Calculus",
                                          "introduction": "An introductory course to Calculus covering limits, derivatives, and integrals.",
                                          "classroom": "201",
                                          "day": "Monday",
                                          "start_time": "09:00:00",
                                          "end_time": "10:00:00",
                                          "teacher_info": {
                                            "id": 2,
                                            "email": "b@gmail.com",
                                            "name": "Bob",
                                            "department": "MATH"
                                          }
                                        },
                                        {
                                          "id": 2,
                                          "course_name": "Linear Algebra",
                                          "introduction": "A fundamental course in Linear Algebra focusing on vector spaces, linear transformations, and matrix theory.",
                                          "classroom": "111",
                                          "day": "Friday",
                                          "start_time": "14:00:00",
                                          "end_time": "15:00:00",
                                          "teacher_info": {
                                            "id": 2,
                                            "email": "b@gmail.com",
                                            "name": "Bob",
                                            "department": "MATH"
                                          }
                                        }
                                      ]
                                    }                            
                                    """)
                    )),
    })
    @GetMapping("/courses")
    public ResponseEntity<?> getCourses() {
        List<CourseDto> courseDtos = courseService.getCourses();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject<>(courseDtos));
    }

    @Operation(summary = "Create a new course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "msg": "Create new course successfully."
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": "The teacher doesn't exist."
                                    }
                                     """)))
    })
    @PostMapping(path = "/course", consumes = {"application/json"})
    public ResponseEntity<?> createCourse(@Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CourseForm.class),
                    examples = @ExampleObject(value = """
                            {
                              "name": "Geometry",
                              "introduction": "An in-depth study of Geometry covering points, lines, angles, and shapes.",
                              "classroom": "301",
                              "day": "Monday",
                              "start_time": "15:00",
                              "end_time": "16:30",
                              "teacher_id": 1
                            }                    
                            """)))
                                          CourseForm form,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> {
                errorMessage.append(error.getDefaultMessage()).append("; ");
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errorMessage.toString()));
        }

        try {
            courseService.saveCourse(form);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Create a new course successfully."));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Update an existing course information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "msg": "Update course successfully."
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": "The course with id:1000 doesn't exist."
                                    }
                                     """)))
    })
    @PutMapping(path = "/course/{id}", consumes = {"application/json"})
    public ResponseEntity<?> updateCourse(@Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CourseForm.class),
                    examples = @ExampleObject(value = """
                            {
                              "name": "Geometry",
                              "introduction": "An in-depth study of Geometry covering points, lines, angles, and shapes.",
                              "classroom": "301",
                              "day": "Monday",
                              "start_time": "15:00",
                              "end_time": "16:30",
                              "teacher_id": 1
                            }                    
                            """)))
                                          CourseForm form,
                                          @PathVariable long id,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> {
                errorMessage.append(error.getDefaultMessage()).append("; ");
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errorMessage.toString()));
        }
        try {
            courseService.updateCourse(id, form);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Update course successfully."));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Delete a course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "msg": "Delete course(id:1) successfully."
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "error": "The course with id:1234 doesn't exist."
                                    }
                                     """)))
    })
    @DeleteMapping(path = "/course/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.status(HttpStatus.OK).
                    body(new SuccessResponse("Delete course(id:" + id + ") successfully."));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }
}
