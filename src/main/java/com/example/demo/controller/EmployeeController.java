package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

@RestController
@RequestMapping("employee")
public class EmployeeController {
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@GetMapping("{id}")
	public ResponseEntity<Employee> getEmployee(@PathVariable int id) {
		Optional<Employee> emp = employeeRepository.findById(id);
		if(emp.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(emp.get());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}
	@GetMapping("")
	public List<Employee> getAllEmployee(@PathVariable int id) {
		List<Employee> empList =  new ArrayList<Employee>();
		employeeRepository.findAll().forEach(empList :: add);
		return empList;
	}
	
	
	@PostMapping("create")
	public ResponseEntity<String>  createEmployee(@RequestBody Employee emp) {
		emp = employeeRepository.save(emp);
		if(emp != null && emp.getId()>0) {
				 return ResponseEntity.status(HttpStatus.CREATED).body("employee "+ emp.getName()+" saved sucessfully with Employee id "+ emp.getId());
			}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem while Creating employee, Please try again");
	}
	
	@DeleteMapping("delete/{id}")
	public ResponseEntity<String>  deleteEmployee(@PathVariable int id) {
		try {
			employeeRepository.deleteById(id);
		}catch (EmptyResultDataAccessException ex) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Employee not present. Please provide the valid employee id");
		}catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Problem while deleting employee, Please try again");
		}
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("employee Deleted");
	}

}
