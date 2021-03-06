package ua.lviv.iot.spring.first.rest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.lviv.iot.spring.first.business.StudentService;
import ua.lviv.iot.spring.first.rest.model.Student;

@RequestMapping("/students")
@RestController
public class StudentsController {
    private Map<Integer, Student> students = new HashMap<>();
    private AtomicInteger idCounter = new AtomicInteger();
    @Autowired
    private StudentService studentService;

    @GetMapping(path = "/{id}")
    public Student getStudent(final @PathVariable("id") Integer studentID) {
        System.out.println(studentID);
        return students.get(studentID);
    }

    @GetMapping
    public List<Student> getStudents(
            final @RequestParam(name = "firstName", required = false) String firstName,
            final @RequestParam(name = "lastName", required = false) String lastName) {
        if (firstName == null || lastName == null) {
            return studentService.findAll();
        }
        return studentService.getAllByFirstNameAndLastName(firstName, lastName);
    }

    @PostMapping
    public Student createStudent(final @RequestBody Student student) {
        student.setId(idCounter.incrementAndGet());
        studentService.createStudent(student);
        return student;
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Student> deleteStudent(final @PathVariable("id") Integer studentID) {
        HttpStatus status = students.remove(studentID) == null ? HttpStatus.NOT_FOUND
                : HttpStatus.OK;
        return ResponseEntity.status(status).build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Student> updateStudent(final @PathVariable("id") Integer studentID,
                                                 final @RequestBody Student student) {
        student.setId(studentID);
        HttpStatus status = students.put(student.getId(), student) == null ? HttpStatus.NOT_FOUND
                : HttpStatus.OK;
        return ResponseEntity.status(status).build();
    }

}