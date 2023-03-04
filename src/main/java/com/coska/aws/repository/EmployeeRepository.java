package com.coska.aws.repository;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import com.coska.aws.entity.Employee;

@EnableScan
public interface EmployeeRepository extends CrudRepository<Employee, String> {

}
