package com.cbo.sso.services;

import com.cbo.sso.models.ADUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.SearchScope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserDetailService {

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
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public List<ADUserDetails> getDetail(String samAccountName){
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(adUrl + ":" + adPort);
        contextSource.setUserDn(ldapDn);
        contextSource.setPassword(adPassword);
        contextSource.setReferral("follow");
        contextSource.afterPropertiesSet();
        LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
        LdapQuery headofficequery = org.springframework.ldap.query.LdapQueryBuilder.query()
                .base(headOfficeGroupSearchBase)
                .searchScope(SearchScope.SUBTREE)
                .countLimit(10)
                .attributes("*")
                .where("sAMAccountName")
                .is(samAccountName);

        LdapQuery districtQuery = org.springframework.ldap.query.LdapQueryBuilder.query()
                .base(districtGroupSearchBase)
                .searchScope(SearchScope.SUBTREE)
                .countLimit(10)
                .attributes("*")
                .where("sAMAccountName")
                .is(samAccountName);

        LdapQuery office365Query = org.springframework.ldap.query.LdapQueryBuilder.query()
                .base(office365GroupSearchBase)
                .searchScope(SearchScope.SUBTREE)
                .countLimit(10)
                .attributes("*")
                .where("sAMAccountName")
                .is(samAccountName);

        LdapQuery stagedUserQuery = org.springframework.ldap.query.LdapQueryBuilder.query()
                .base(office365GroupSearchBase)
                .searchScope(SearchScope.SUBTREE)
                .countLimit(10)
                .attributes("*")
                .where("sAMAccountName")
                .is(samAccountName);


        System.out.println("Searching: " + samAccountName + " at Head Office Level");
        List<ADUserDetails> userDetailsList = ldapTemplate.search(headofficequery, (ContextMapper<ADUserDetails>) ctx -> {
            DirContextAdapter context = (DirContextAdapter) ctx; // Cast to DirContextAdapter
            ADUserDetails adUserDetails = new ADUserDetails();
            adUserDetails.setDn(context.getStringAttribute("distinguishedName"));
            adUserDetails.setUsername(context.getStringAttribute("sAMAccountName"));
            adUserDetails.setPassword(context.getStringAttribute("password"));
            adUserDetails.setCompany(context.getStringAttribute("company"));
            adUserDetails.setMail(context.getStringAttribute("mail"));
            adUserDetails.setDepartment(context.getStringAttribute("department"));
            adUserDetails.setManager(context.getStringAttribute("manager"));
            adUserDetails.setMobile(context.getStringAttribute("mobile"));

            return adUserDetails;
        });
        if (userDetailsList.isEmpty()) {
            System.out.println("Searching: " + samAccountName + " at District Level");
            userDetailsList = ldapTemplate.search(districtQuery, (ContextMapper<ADUserDetails>) (ctx) -> {
                DirContextAdapter context = (DirContextAdapter)ctx;
                ADUserDetails adUserDetails = new ADUserDetails();
                adUserDetails.setDn(context.getStringAttribute("distinguishedName"));
                adUserDetails.setUsername(context.getStringAttribute("sAMAccountName"));
                adUserDetails.setPassword(context.getStringAttribute("password"));
                adUserDetails.setCompany(context.getStringAttribute("company"));
                adUserDetails.setMail(context.getStringAttribute("mail"));
                adUserDetails.setDepartment(context.getStringAttribute("department"));
                adUserDetails.setManager(context.getStringAttribute("manager"));
                adUserDetails.setMobile(context.getStringAttribute("mobile"));
                return adUserDetails;
            });
        }
        if (userDetailsList.isEmpty()) {
            System.out.println("Searching: " + samAccountName + " at Office 365 Level");
            userDetailsList = ldapTemplate.search(office365Query, (ContextMapper<ADUserDetails>) (ctx) -> {
                DirContextAdapter context = (DirContextAdapter)ctx;
                ADUserDetails adUserDetails = new ADUserDetails();
                adUserDetails.setDn(context.getStringAttribute("distinguishedName"));
                adUserDetails.setUsername(context.getStringAttribute("sAMAccountName"));
                adUserDetails.setPassword(context.getStringAttribute("password"));
                adUserDetails.setCompany(context.getStringAttribute("company"));
                adUserDetails.setMail(context.getStringAttribute("mail"));
                adUserDetails.setDepartment(context.getStringAttribute("department"));
                adUserDetails.setManager(context.getStringAttribute("manager"));
                adUserDetails.setMobile(context.getStringAttribute("mobile"));
                return adUserDetails;
            });
        }

        if (userDetailsList.isEmpty()) {
            System.out.println("Searching: " + samAccountName + " at Staged User Level");
            userDetailsList = ldapTemplate.search(office365Query, (ContextMapper<ADUserDetails>) (ctx) -> {
                DirContextAdapter context = (DirContextAdapter)ctx;
                ADUserDetails adUserDetails = new ADUserDetails();
                adUserDetails.setDn(context.getStringAttribute("distinguishedName"));
                adUserDetails.setUsername(context.getStringAttribute("sAMAccountName"));
                adUserDetails.setPassword(context.getStringAttribute("password"));
                adUserDetails.setCompany(context.getStringAttribute("company"));
                adUserDetails.setMail(context.getStringAttribute("mail"));
                adUserDetails.setDepartment(context.getStringAttribute("department"));
                adUserDetails.setManager(context.getStringAttribute("manager"));
                adUserDetails.setMobile(context.getStringAttribute("mobile"));
                return adUserDetails;
            });
        }

        return userDetailsList;
    }

}
