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
@Table(name = "modules")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable =false , updatable = false , unique = true)
    private Long id;
    private String code;
    private String name;
    private String url;
    private Boolean status;
    public Module(String code, String name, String url, Boolean status) {
        this.code = code;
        this.name = name;
        this.url = url;
        this.status = status;
    }
}
