//package com.cbo.sso.controllers;
//import com.cbo.sso.models.*;
//import com.cbo.sso.services.EmployeeService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
//
//@RestController
//@RequestMapping("/employee")
//public class EmployeeController {
//    @Autowired
//    private ObjectMapper objectMapper;
//    private final EmployeeService employeeService;
//
//    public EmployeeController(EmployeeService employeeService) {
//        this.employeeService = employeeService;
//    }
//    @GetMapping("/all")
//    public ResponseEntity<List<Employee>> getAllEmployees(){
//        List<Employee> employees = employeeService.findAllEmployee();
//        return new ResponseEntity<>(employees, HttpStatus.OK);
//    }
//    @GetMapping("/find/{id}")
//    public ResponseEntity<Employee> getEmployeeId (@PathVariable("id") Long id) {
//        Employee employee = employeeService.findEmployeeById(id);
//        return new ResponseEntity<>(employee, HttpStatus.OK);
//    }
//
//    @GetMapping("/getEmployeeByFullName/{fullname}")
//    public ResponseEntity<Employee> getEmployeeByFullName (@PathVariable("fullname") String fullname) {
//        Employee employee = employeeService.findEmployeeByFullName(fullname);
//        return new ResponseEntity<>(employee, HttpStatus.OK);
//    }
//
//    @GetMapping("/avatarImagePath/{id}")
//    public ResponseEntity<byte[]> getEmployeeAvatarImage(@PathVariable long id) {
//        System.out.println("Inside getEmployeeAvatarImage = employee"+id);
//        Employee employee = employeeService.findEmployeeById(id);
//        String imagePath = employee.getEmployeeImage();
//        System.out.println("employee_image = " + imagePath);
//        String imageFilePath = "./user-photos/employee/"+id+"/"+imagePath;
//        try {
//            byte[] imageBytes = Files.readAllBytes(Paths.get(imageFilePath));
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.IMAGE_JPEG);
//            headers.setContentLength(imageBytes.length);
//            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @GetMapping("/signatureImagePath/{id}")
//    public ResponseEntity<byte[]> getEmployeeSignatureImage(@PathVariable long id) {
//        System.out.println("Inside getEmployeeSignatureImage = employee"+id);
//        Employee employee = employeeService.findEmployeeById(id);
//        String imagePath = employee.getSignatureImage();
//        System.out.println("signature_image = " + imagePath);
//        String imageFilePath = "./user-photos/employee/"+id+"/"+imagePath;
//        try {
//            System.out.println(imageFilePath);
//            byte[] imageBytes = Files.readAllBytes(Paths.get(imageFilePath));
//            System.out.println(imageBytes);
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.IMAGE_JPEG);
//            headers.setContentLength(imageBytes.length);
//            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
//        } catch (IOException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//}
//
