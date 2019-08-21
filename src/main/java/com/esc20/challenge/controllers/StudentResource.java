package com.esc20.challenge.controllers;

import com.esc20.challenge.entities.Student;
import com.esc20.challenge.exceptions.StudentNotFoundException;
import com.esc20.challenge.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.*;

@RestController
public class StudentResource {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/students")
    public List<Student> retrieveAllStudents(@RequestParam(required=false) Long studentID, @RequestParam(required=false) Integer schoolYr,
                                             @RequestParam(required=false) Integer campus, @RequestParam(required=false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate entryDate,
                                             @RequestParam(required=false) Integer gradeLevel, @RequestParam(required=false) String name) {

        Student student = new Student();
        student.setStudentID(studentID);
        student.setSchoolYr(schoolYr);
        student.setCampus(campus);
        student.setEntryDate(entryDate);
        student.setGradeLevel(gradeLevel);
        student.setName(name);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("studentID", exact())
                .withMatcher("schoolYr", exact())
                .withMatcher("campus", exact())
                .withMatcher("entryDate", exact())
                .withMatcher("gradeLevel", exact())
                .withMatcher("name", contains());

        Example<Student> example = Example.of(student, matcher);

        return studentRepository.findAll(example, Sort.by(Sort.Direction.ASC, "studentID"));
    }

    @GetMapping("/students/{id}")
    public Student retrieveStudent(@PathVariable long id) throws StudentNotFoundException {
        Optional<Student> student = studentRepository.findById(id);

        if (!student.isPresent())
            throw new StudentNotFoundException(id);

        return student.get();
    }

    @DeleteMapping("/students/{id}")
    public void deleteStudent(@PathVariable long id) {
        studentRepository.deleteById(id);
    }

    @PostMapping("/students")
    public ResponseEntity<Object> createStudent(@RequestBody Student student) {
        Student savedStudent = studentRepository.save(student);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedStudent.getStudentID()).toUri();

        return ResponseEntity.created(location).build();

    }

    @PutMapping("/students/{id}")
    public ResponseEntity<Object> updateStudent(@RequestBody Student student, @PathVariable long id) {

        Optional<Student> studentOptional = studentRepository.findById(id);

        if (!studentOptional.isPresent())
            return ResponseEntity.notFound().build();

        student.setStudentID(id);

        studentRepository.save(student);

        return ResponseEntity.noContent().build();
    }

}
