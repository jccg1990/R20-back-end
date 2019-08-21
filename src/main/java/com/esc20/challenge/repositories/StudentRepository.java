package com.esc20.challenge.repositories;

import com.esc20.challenge.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
