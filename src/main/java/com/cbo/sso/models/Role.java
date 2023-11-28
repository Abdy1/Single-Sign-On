package com.cbo.sso.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(length = 30, unique = true)
    private ERole name;
    @Column
    private Long parentRoleId;
    @ManyToOne
    private Module module;

    public Role(ERole name, Module module, Long parentRoleId) {
        this.name = name;
        this.module = module;
        this.parentRoleId = parentRoleId;
    }

}
