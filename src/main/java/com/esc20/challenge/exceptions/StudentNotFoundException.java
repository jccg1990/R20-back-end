package com.esc20.challenge.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class StudentNotFoundException extends Exception {

    public StudentNotFoundException(Long id) {
        super("Student not found: Id " + id);
    }

}
