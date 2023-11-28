package com.cbo.sso.models;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "active")
    private Boolean active;
    @Column(name = "createdAt")
    private String createdAt;
    @Column(name = "updatedAt")
    private String updatedAt;




    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private String employeeImage;
    private String signatureImage;

    @Column(name = "isSupervisor")
    private boolean isSupervisor;



//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "employee_id", referencedColumnName = "id")
//    private Employee employee;

//        public void setIsSupervisor(boolean isSupervisor) {
//        this.isSupervisor = isSupervisor;
//    }


}
