
//package com.cbo.sso.models;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "employees")
//public class Employee {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY,
//            generator = "employee_sequence")
//    private Long id;
//    private Long employeeId;
//    private String fullName;
//    private String jobTitle;
//    @ManyToOne
//    private OrganizationalUnit organizationalUnit;
//    private Long supervisorId;
//    private String phoneNumber;
//    private String personalEmail;
//    private String companyEmail;
//    private String gender;
//    private String birthDate;
//    private String employeeImage;
//    private String signatureImage;
//    private Boolean active;
//
////    @OneToOne(mappedBy = "employee")
////    @JsonIgnore
////    private User user;
//
//    @Transient
//    @JsonProperty
//    private boolean isSupervisor;
//    public Employee(Long employeeId, String fullName, String jobTitle, OrganizationalUnit organizationalUnit, Long supervisorId, String phoneNumber, String personalEmail, String companyEmail, String gender, String birthDate, String employeeImage, String signatureImage, boolean active) {
//        this.employeeId = employeeId;
//        this.fullName = fullName;
//        this.jobTitle = jobTitle;
//        this.organizationalUnit = organizationalUnit;
//        this.supervisorId = supervisorId;
//        this.phoneNumber = phoneNumber;
//        this.personalEmail = personalEmail;
//        this.companyEmail = companyEmail;
//        this.gender = gender;
//        this.birthDate = birthDate;
//        this.employeeImage = employeeImage;
//        this.signatureImage = signatureImage;
//        this.active = active;
//    }
//
//
//
//    public void setIsSupervisor(boolean isSupervisor) {
//        this.isSupervisor = isSupervisor;
//    }
//
//
//}
//
