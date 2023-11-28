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
@Table(name = "organizational_units")
public class OrganizationalUnit {
    @Id
    private Long id;
    private String code;
    private String name;
    private String mnemonic;
    private String area;
    private String town;
    private String telephone;
    @ManyToOne
    private SubProcess subProcess;
}
