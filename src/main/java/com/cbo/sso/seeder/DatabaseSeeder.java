package com.cbo.sso.seeder;

import com.cbo.sso.models.*;
import com.cbo.sso.models.Module;
import com.cbo.sso.models.Process;
import com.cbo.sso.repositories.*;
import com.cbo.sso.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.*;

import static com.cbo.sso.models.ERole.*;

@Component
public class DatabaseSeeder {
    private JdbcTemplate jdbcTemplate;
    private ModuleService moduleService;
    private RoleRepository roleRepository;
    private ModuleRepository moduleRepository;
    private RoleService roleService;
    private UserRepository userRepository;
//    private OrganizationalUnitService organizationalUnitService;
    private Role role;
    @Autowired
    public DatabaseSeeder(
            JdbcTemplate jdbcTemplate,
            RoleRepository roleRepository,
            ModuleRepository moduleRepository,
            RoleService roleService,
            ModuleService moduleService,
            UserRepository userRepository
//            OrganizationalUnitService organizationalUnitService
    ){
        this.jdbcTemplate = jdbcTemplate;
        this.roleRepository = roleRepository;
        this.moduleRepository = moduleRepository;
        this.roleService = roleService;
        this.moduleService = moduleService;
        this.userRepository = userRepository;
//        this.organizationalUnitService = organizationalUnitService;
    }

    @PostConstruct
    public void seed() {
        seedModulesTable("SSO", "Single Sign-On", "http://10.1.125.58:8081", true);
        seedModulesTable("EMS", "Employee Management System", "http://10.1.125.58:8082", true);
        seedModulesTable("CC", "Compliance Check", "http://10.1.125.58:8083", true);
        seedModulesTable("ICMS", "Internal Control Management System", "http://10.1.125.58:8084", true);
        seedModulesTable("SASV", "Signature and Stamp Verification", "http://10.1.125.58:8085", true);
        seedModulesTable("MEMO", "Inter Office Memorandum", "http://10.1.125.58:8086", true);
        seedModulesTable("ECX", "Ethiopian Commodity of Exchange","http://10.1.125.58:8087", true);
        seedModulesTable("CMS", "COB Monitoring System","http://10.1.125.58:8088", true);
        seedModulesTable("AMS", "Audit Management System","http://10.1.125.58:8089", true);
        seedModulesTable("SMS", "SMS Management System","http://10.1.125.58:8090", true);

        seedRolesTable(ROLE_SUPER_ADMIN, null);
        seedRolesTable(ROLE_EMS_ADMIN, ROLE_SUPER_ADMIN);
        seedRolesTable(ROLE_EMS_USER, ROLE_EMS_ADMIN);
        seedRolesTable(ROLE_CC_ADMIN, ROLE_SUPER_ADMIN);
        seedRolesTable(ROLE_CC_USER, ROLE_CC_ADMIN);
        seedRolesTable(ROLE_CC_USER_DELIQUENT, ROLE_CC_ADMIN);
        seedRolesTable(ROLE_ICMS_ADMIN, ROLE_SUPER_ADMIN);
        seedRolesTable(ROLE_ICMS_DISTRICT_IC, ROLE_ICMS_ADMIN);
        seedRolesTable(ROLE_ICMS_BRANCH_IC, ROLE_ICMS_DISTRICT_IC);
        seedRolesTable(ROLE_ICMS_BRANCH_MANAGER, ROLE_ICMS_DISTRICT_IC);
        seedRolesTable(ROLE_ICMS_PROVISION, ROLE_ICMS_ADMIN);
        seedRolesTable(ROLE_SASV_ADMIN, ROLE_SUPER_ADMIN);
        seedRolesTable(ROLE_SASV_USER, ROLE_SASV_ADMIN);
        seedRolesTable(ROLE_MEMO_ADMIN, ROLE_SUPER_ADMIN);
        seedRolesTable(ROLE_MEMO_USER, ROLE_MEMO_ADMIN);
        seedRolesTable(ROLE_ECX_ADMIN, ROLE_SUPER_ADMIN);
        seedRolesTable(ROLE_ECX_USER, ROLE_ECX_ADMIN);
        seedRolesTable(ROLE_CMS_ADMIN, ROLE_SUPER_ADMIN);
        seedRolesTable(ROLE_CMS_USER, ROLE_CMS_ADMIN);
        seedRolesTable(ROLE_AMS_ADMIN, ROLE_SUPER_ADMIN);
        seedRolesTable(ROLE_SMS_ADMIN, ROLE_SUPER_ADMIN);
        seedRolesTable(ROLE_SMS_USER, ROLE_SMS_ADMIN);


        this.role = this.roleService.findRoleById(7L);
        Set<Role> setOfRoles1 = new HashSet<>();
        setOfRoles1.add(this.role);

//        seedUsersAndEmployeesTable(1L,  "debbym");
        seedUsers(10008202L,"debbym",true,  new Date().toLocaleString(), new Date().toLocaleString(), setOfRoles1, "","",true);
//        seedUsersAndEmployeesTable(1L, "ephremd");
//        seedUsersAndEmployeesTable(1L, "HO", 8L,"PS08", "Information Systems Process".toUpperCase(), 22L, "SP22", "Core Systems".toUpperCase(), 1359L, "Application Team".toUpperCase(), 10001052L, 10007299L, "ABDY FIKADU NAMIE", "GRADUATE TRAINEE", "Male", "abdyf");
//        seedUsersAndEmployeesTable(1L,"jibrilk");
    }
    @Transactional
    public void seedModulesTable(String code, String name, String url, Boolean status) {
        String sql = "SELECT name FROM modules M WHERE M.name = ? LIMIT 1";
        List<Module> m = jdbcTemplate.query(sql, new Object[]{name}, (resultSet, rowNum) -> null);

        if(m == null || m.size() == 0) {
            Module module = new Module(code, name, url, status);
            moduleRepository.save(module);
        }
        else {
            System.out.println("Module Seeding Not Required");
        }
    }
    @Transactional
    public void seedRolesTable(ERole inputRole, ERole parentERole) {
        String sql = "SELECT name FROM roles R WHERE R.name = ? LIMIT 1";
        List<Role> r = jdbcTemplate.query(sql, new Object[]{inputRole.toString()}, (resultSet, rowNum) -> null);
        Module module = null;

        if (r == null || r.size() == 0) {
            if (inputRole == ROLE_SUPER_ADMIN) {
                module = moduleService.findModuleByCode("SSO");
            } else {
                module = moduleService.findModuleByCode(inputRole.toString().split("_")[1]);
            }
            Role parentRole = roleService.findRoleByName(parentERole);
            Role role;

            if (parentRole == null) {
                role = new Role(inputRole, module, null);
            } else {
                role = new Role(inputRole, module, parentRole.getId());
            }

            roleRepository.save(role);
        }
        else {
            System.out.println("Role Seeding Not Required");
        }
    }
    @Transactional
    public void seedUsers(Long employee_id,String username, Boolean active, String createdAt, String updatedAt, Set<Role> roles, String employeeImage, String signatureImage,Boolean isSupervisor) {
        String sql = "SELECT username FROM users U WHERE U.username = ? LIMIT 1";
        List<User> u = jdbcTemplate.query(sql, new Object[]{username}, (resultSet, rowNum) -> null);
        if(u == null || u.size() == 0) {
            User user = new User(employee_id,username, active, createdAt, updatedAt, roles,"","",isSupervisor);
            userRepository.save(user);
        } else {
            System.out.println("User Seeding Not Required");
        }
    }

//    public void seedUsersAndEmployeesTable(
//            Long employeeId,
//            String username
//    ) {
//
//        this.role = this.roleService.findRoleById(7L);
//        Set<Role> setOfRoles1 = new HashSet<>();
//        setOfRoles1.add(this.role);
//        seedUsersTable(employeeId,username,true,  new Date().toLocaleString(), new Date().toLocaleString(), setOfRoles1, "","");
//
//    }
}

