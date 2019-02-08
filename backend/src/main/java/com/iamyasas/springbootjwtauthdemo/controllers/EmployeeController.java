package com.iamyasas.springbootjwtauthdemo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iamyasas.springbootjwtauthdemo.models.Employee;
import com.iamyasas.springbootjwtauthdemo.services.EmployeeService;

@RestController
//@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService service;
	
	//@PreAuthorize("hasRole('ADMIN') or hasRole('EDITOR')")
	@GetMapping("/employee")
	public ResponseEntity<Employee[]> getEmployees(@RequestParam(required = false) String name) {
		return ResponseEntity.ok().body(service.getEmployees(name));
	}
	
	//@PreAuthorize("#employeeID == authentication.principal")
	@GetMapping("/emps/{employeeID}")
	public ResponseEntity<Employee> getEmployee(@PathVariable String employeeID) {
		return ResponseEntity.ok().body(service.getEmployee(Integer.parseInt(employeeID)));
	}
	
	
}
