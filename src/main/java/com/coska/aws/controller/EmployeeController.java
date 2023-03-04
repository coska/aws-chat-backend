package com.coska.aws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coska.aws.entity.Employee;
import com.coska.aws.repository.EmployeeRepository;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping("/example/employees")
    public Iterable<Employee> getEmployeeList() {
        return employeeRepository.findAll();
    }

}
