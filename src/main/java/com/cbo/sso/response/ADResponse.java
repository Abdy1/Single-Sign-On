package com.cbo.sso.response;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class ADResponse {
    private String dn;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private String company;
    private String mail;
    private String department;
    private String manager;
    private String samAccountName;
    private boolean enabled;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
}