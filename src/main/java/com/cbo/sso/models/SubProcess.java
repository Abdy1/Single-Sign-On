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
@Table(name = "sub_processes")
public class SubProcess {
    @Id
    private Long id;
    private String code;
    private String name;
    @ManyToOne
    private Process process;
    @ManyToOne
    private WorkCenter workCenter;
}
