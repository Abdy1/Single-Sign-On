package com.cbo.sso.controllers;

import com.cbo.sso.models.Role;
import com.cbo.sso.models.Module;
import com.cbo.sso.repositories.RoleRepository;
import com.cbo.sso.services.ModuleService;
import com.cbo.sso.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/role")
public class RoleController {
    private final RoleService roleService;
    private final ModuleService moduleService;

    public RoleController(RoleService roleService, ModuleService moduleService) {
        this.roleService = roleService; this.moduleService = moduleService;
    }

    @Autowired
    RoleRepository roleRepository;
    @GetMapping("/all/{moduleName}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'EMS_ADMIN', 'CC_ADMIN', 'ICMS_ADMIN', 'SASV_ADMIN', 'MEMO_ADMIN', 'ECX_ADMIN', 'CMS_ADMIN' )")
    public ResponseEntity<List<Role>> getAllRoles(@PathVariable("moduleName") String moduleName){
        List<Role> roles = roleService.findAllRole(moduleService.findModuleByName(moduleName));
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/getRoles")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'EMS_ADMIN', 'CC_ADMIN', 'ICMS_ADMIN', 'SASV_ADMIN', 'MEMO_ADMIN', 'ECX_ADMIN', 'CMS_ADMIN', 'ROLE_ICMS_BRANCH_MANAGER' )")
    public ResponseEntity<List<Role>> getRoles() {
        List<Role> roles = roleRepository.findAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
    @GetMapping("/find/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'EMS_ADMIN', 'CC_ADMIN', 'ICMS_ADMIN', 'SASV_ADMIN', 'MEMO_ADMIN', 'ECX_ADMIN', 'CMS_ADMIN' )")
    public ResponseEntity<Role> getRoleId (@PathVariable("id") Long id) {
        Role role= roleService.findRoleById(id);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @PostMapping("/getRolesForModules")
    public List<Role> getRolesForModules(@RequestBody List<Module> modules) {
        return roleService.getRolesForModules(modules);
    }
    @GetMapping("/getByModuleId/{moduleId}")
    public List<Role> getRoleByModuleId(@PathVariable Long moduleId) {
        return roleService.getRoleByModuleId(moduleId);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<Role> addRole(@RequestBody Role role) {
        Role newRole =roleService.addRole(role);
        return new ResponseEntity<>(newRole, HttpStatus.CREATED);
    }
    @PutMapping("/update")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Role> updateRole(@RequestBody Role role) {
        Role updateRole =roleService.updateRole(role);
        return new ResponseEntity<>(updateRole, HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> deleteRole(@PathVariable("id") Long id) {
        roleService.deleteRole(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
