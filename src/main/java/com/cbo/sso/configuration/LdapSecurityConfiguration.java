//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.cbo.sso.configuration;

import com.cbo.sso.models.ADUserDetails;
import com.cbo.sso.models.ERole;
import com.cbo.sso.models.Role;
import com.cbo.sso.models.User;
import com.cbo.sso.repositories.UserRepository;
import com.cbo.sso.response.JwtResponse;
import com.cbo.sso.security.JwtAuthenticationEntryPoint;
import com.cbo.sso.security.JwtAuthenticationFilter;
import com.cbo.sso.security.JwtUtils;
import com.cbo.sso.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.cbo.sso.models.ERole.ROLE_SASV_ADMIN;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
@Order(1)
public class LdapSecurityConfiguration extends WebSecurityConfigurerAdapter {
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
    @Value("${spring.ldap.base}")
    private String ldapBase;
    @Value("${spring.security.ldap.user-search-base}")
    private String userSearchBase;
    @Value("${spring.ldap.head-office-group-search-base}")
    private String headOfficeGroupSearchBase;
    @Value("${spring.ldap.district-group-search-base}")
    private String districtGroupSearchBase;

    @Value("${spring.ldap.Staged.Users-group-search-base}")
    private String Office365GroupSearchBase;
    @Value("${spring.ldap.Office.365-group-search-base}")
    private String StagedUsersGroupSearchBase;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    UserRepository userRepository;

    public LdapSecurityConfiguration() {
    }

    public UserDetailsContextMapper userDetailsContextMapper() {
        return new LdapUserDetailsMapper() {
            public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
                LdapUserDetails ldapUser = (LdapUserDetails)super.mapUserFromContext(ctx, username, authorities);
                String company = ctx.getStringAttribute("company");
                String mail = ctx.getStringAttribute("mail");
                String manager = ctx.getStringAttribute("manager");
                String department = ctx.getStringAttribute("department");
                String mobile = ctx.getStringAttribute("mobile");
                ADUserDetails userDetails = new ADUserDetails(ldapUser, company, mail, manager, department, mobile);
                return userDetails;
            }
        };
    }

    protected void configure(HttpSecurity http) throws Exception {
        ((HttpSecurity)((HttpSecurity)((HttpSecurity)((FormLoginConfigurer)((FormLoginConfigurer)((FormLoginConfigurer)((HttpSecurity)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((HttpSecurity)http.cors().and()).authorizeRequests().antMatchers(new String[]{"/login"})).permitAll().anyRequest()).authenticated().and()).formLogin().successHandler((request, response, authentication) -> {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String username = authentication.getName();
            if (username != null) {
                UserService userService = new UserService(this.userRepository);
                User user = userService.findUserByUsername(username);
//                if(user.getRoles() !=null){
//                    Set<Role> roles = null;
//                    user.setRoles(roles);
//                }
                Set<Role> rolesSet = user.getRoles();
                List<ERole> rolesEnumList = (List)rolesSet.stream().map(Role::getName).collect(Collectors.toList());
                List<String> rolesList = (List)rolesEnumList.stream().map(Enum::name).collect(Collectors.toList());
                Collection<GrantedAuthority> grantedAuthorities = (Collection)rolesList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                String token = this.jwtUtils.createToken(username, grantedAuthorities);
                if (this.jwtUtils.validateToken(token)) {
                    JwtResponse jwtResponse = new JwtResponse();
                    jwtResponse.setUser(user);
                    jwtResponse.setAccessToken(token);
                    response.setStatus(200);
                    ObjectMapper objectMapper = new ObjectMapper();
                    response.getWriter().write(objectMapper.writeValueAsString(jwtResponse));
                }
            }

        })).failureHandler((request, response, exception) -> {
            response.setStatus(401);
            if (exception instanceof BadCredentialsException) {
                response.getWriter().write("Invalid username or password");
            } else if (exception instanceof LockedException) {
                response.getWriter().write("Your account is locked");
            } else {
                response.getWriter().write("Authentication failed for an unknown reason");
            }
        })).permitAll()).and()).csrf().disable()).exceptionHandling().authenticationEntryPoint(this.jwtAuthenticationEntryPoint).and()).sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(this.jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    public void configure(AuthenticationManagerBuilder authMake) throws Exception {
        authMake.ldapAuthentication()
                .userSearchFilter(this.userSearchBase)
                .userDetailsContextMapper(this.userDetailsContextMapper())
                .groupSearchBase(this.districtGroupSearchBase)
                .groupSearchBase(this.Office365GroupSearchBase)
                .groupSearchBase(this.headOfficeGroupSearchBase)
                .groupSearchBase(this.StagedUsersGroupSearchBase)

                .userSearchBase(this.ldapBase)
                .contextSource().url(this.adUrl)
                .port(Integer.parseInt(this.adPort))
                .managerDn(this.adUsernamePrefix + this.adUsername).managerPassword(this.adPassword);
    }
}
