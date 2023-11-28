package com.cbo.sso.services;


import com.cbo.sso.exceptions.NoSuchUserExistsException;
import com.cbo.sso.exceptions.ResourceNotFoundException;
import com.cbo.sso.exceptions.UserAlreadyExistsException;
import com.cbo.sso.models.ERole;
import com.cbo.sso.models.Role;
import com.cbo.sso.models.Module;
import com.cbo.sso.repositories.ModuleRepository;
import com.cbo.sso.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    @Autowired
    private ModuleRepository moduleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public Role addRole(Role role){
        Role existingRole = roleRepository.findByName(role.getName());

        if (existingRole == null) {

            Role newrole = roleRepository.save(role);
            return newrole;
        }
        else
            throw new UserAlreadyExistsException(
                    "Role already exists!");
    }

    public List<Role> findAllRole(Module module){
        return roleRepository.findRoleByModule(module);
    }

    public List<Role> getRolesForModules(List<Module> modules) {
        List<Role> roles = new ArrayList<>();
        for (Module module : modules) {
            List<Role> moduleRoles = roleRepository.findRoleByModule(module);
            roles.addAll(moduleRoles);
        }
        return roles;
    }
    public List<Role> getRoleByModuleId(Long moduleId) {
        Module module = moduleRepository.findById(moduleId).orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + moduleId));
        return roleRepository.findRoleByModule(module);
    }

    public Role updateRole(Role role){
        Role existingRole = roleRepository.findById(role.getId()).orElse(null);

        if (existingRole == null)
            throw new NoSuchUserExistsException("No such role exists!");
        else {
            existingRole.setName(role.getName());
            Role updatedRole = roleRepository.save(existingRole);

            return updatedRole;
        }
    }

    public Role findRoleById(Long id){
        return roleRepository.findById(id).orElseThrow(() -> new NoSuchUserExistsException("Role by id "+ id + " was not found."));
    }

    public String deleteRole(Long id){
        Role existingRole = roleRepository.findById(id).orElse(null);

        if (existingRole == null)
            throw new NoSuchUserExistsException("No such role exists!");
        else {
            roleRepository.deleteById(id);

            return "Record deleted Successfully.";
        }
    }

    public Role findRoleByName(ERole parentERole) {
        return roleRepository.findByName(parentERole);
    }
}
