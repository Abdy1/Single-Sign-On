package com.cbo.sso.models;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ADUserDetails implements UserDetails {
    private  String dn;
    private  String username;
    private  String password;
    private  Collection<? extends GrantedAuthority> authorities;
    private  String company;
    private  String mail;
    private  String department;
    private  String manager;
    private  String mobile;

    public ADUserDetails(LdapUserDetails ldapUser, String company, String mail, String manager, String department, String mobile) {
        this.dn = ldapUser.getDn();
        this.username = ldapUser.getUsername();
        this.password = ldapUser.getPassword();
        this.authorities = ldapUser.getAuthorities();
        this.department = department;
        this.manager = manager;
        this.company = company;
        this.mail = mail;
        this.mobile = mobile;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}