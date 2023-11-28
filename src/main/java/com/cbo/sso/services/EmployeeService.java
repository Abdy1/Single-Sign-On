//package com.cbo.sso.services;
//
//import com.cbo.sso.exceptions.NoSuchUserExistsException;
////import com.cbo.sso.models.Employee;
//import com.cbo.sso.models.User;
//import com.cbo.sso.repositories.EmployeeRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class EmployeeService {
//    private final EmployeeRepository employeeRepository;
//
//    public EmployeeService(EmployeeRepository employeeRepository) {
//        this.employeeRepository = employeeRepository;
//    }
//
//    public List<Employee> findAllEmployee(){
//    return employeeRepository.findAll();
//}
//
//    public boolean isSupervisor(Long id) {
//        return employeeRepository.existsBySupervisorId(id);
//    }
//
//    public Employee findEmployeeById(Long id){
//        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new NoSuchUserExistsException("NO Employee PRESENT WITH ID = " + id));
//        boolean isSupervisor = isSupervisor(id);
//        employee.setIsSupervisor(isSupervisor);
//        return employee;
//    }
//
//    public Employee findEmployeeByUser(User user) {
//        return employeeRepository.findByUser(user);
//}
//
//    public String deleteEmployee(Long id){
//
//        Employee existingEmployee = employeeRepository.findById(id).orElse(null);
//        if (existingEmployee == null)
//            throw new NoSuchUserExistsException("No such employee exists!");
//        else {
//            employeeRepository.deleteById(id);
//
//            return "Record deleted Successfully";
//        }
//    }
//
//    public Employee findEmployeeByFullName(String fullname) {
//        for (Employee employee : employeeRepository.findAll()) {
//            if (employee.getFullName().equalsIgnoreCase(fullname)) {
//                return employee;
//            }
//        }
//        return null; // Employee not found
//    }
//}
