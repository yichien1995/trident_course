package com.example.course.controller;

import com.example.course.data.ResponseObject;
import com.example.course.data.dto.TeacherDto;
import com.example.course.data.form.TeacherForm;
import com.example.course.exception.UserNotFoundException;
import com.example.course.exception.ValidationException;
import com.example.course.response.ErrorResponse;
import com.example.course.response.SuccessResponse;
import com.example.course.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/1.0/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public ResponseEntity<?> getTeachers(){
        List<TeacherDto> dtos = teacherService.getTeachers();
        return ResponseEntity.ok().body(new ResponseObject<>(dtos));
    }

    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<?> createTeacher(@Valid @RequestBody TeacherForm form,
                                           BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
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
