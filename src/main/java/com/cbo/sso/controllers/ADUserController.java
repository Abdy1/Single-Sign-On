package com.cbo.sso.controllers;

import com.cbo.sso.models.ADUserDetails;
import com.cbo.sso.models.User;
import com.cbo.sso.services.UserDetailService;
import com.cbo.sso.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.SearchScope;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping

public class ADUserController {
    private final LdapTemplate ldapTemplate;
@Autowired
private UserService userService;
    @Value("${ad.url}")
    private String adUrl;
    @Value("${ad.port}")
    private String adPort;
    @Value("${ad.usernamePrefix}")
    private String adUsernamePrefix;
    @Value("${ad.username}")
    private String adUsername;
    @Value("${ad.password}")
    private String adPassword;
    @Value("${spring.ldap.dn}")
    private String ldapDn;
    @Value("${spring.ldap.base}")
    private String ldapBase;
    @Value("${spring.security.ldap.user-search-base}")
    private String userSearchBase;
    @Value("${spring.ldap.head-office-group-search-base}")
    private String headOfficeGroupSearchBase;

    @Value("${spring.ldap.district-group-search-base}")
    private String districtGroupSearchBase;

    @Value("${spring.ldap.Office.365-group-search-base}")
    private String office365GroupSearchBase;

    @Value("${spring.ldap.Staged.Users-group-search-base}")
    private String stagedUserGroupSearchBase;

    @Autowired
    public ADUserController(LdapTemplate ldapTemplate, UserDetailService userDetailService) {
        this.ldapTemplate = ldapTemplate;

    }
    @Autowired
    UserDetailService userDetailService;

    @GetMapping("/ADUser/search/{samAccountName}")
    public List<ADUserDetails> searchBySamAccountName(@PathVariable String samAccountName) {
        return userDetailService.getDetail(samAccountName);
    }
    @GetMapping("/ADUser/searchById/{id}")
    public List<ADUserDetails> searchByUserId(@PathVariable Long id) {
       User user =  userService.findUserByUserId(id);
       return  searchByUserName(user.getUsername());
    }

    public List<ADUserDetails> searchByUserName(String samAccountName) {
        return userDetailService.getDetail(samAccountName);
    }
}
