package com.cbo.sso.controllers;

import com.cbo.sso.models.Module;
import com.cbo.sso.services.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("module")
public class ModuleController {
    private final ModuleService moduleService;

    @Autowired
    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ICMS_ADMIN', 'ECX_ADMIN', 'ROLE_ICMS_BRANCH_MANAGER')")
    public ResponseEntity<List<Module>> getAllModule(){
        List<Module> modules = moduleService.findAllModule();
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public ResponseEntity<Module> addRole(@RequestBody Module module) {
        Module newModule = moduleService.addModule(module);
        return new ResponseEntity<>(newModule, HttpStatus.CREATED);
    }
    @PutMapping("/update")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Module> updateRModule(@RequestBody Module module) {
        Module updateModule = moduleService.updateModule(module);
        return new ResponseEntity<>(updateModule, HttpStatus.CREATED);
    }
    @GetMapping("/find/{id}")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN', 'SUPER_ADMIN', 'ROLE_ICMS_BRANCH_MANAGER' )")
    public Module getModuleById(@PathVariable("id") Long id){
        return moduleService.findModuleById(id);
    }
    @GetMapping("/findId/{name}")
    public Long getModuleIdByName(@PathVariable("name") String name){
        return moduleService.findModuleIdByName(name);
    }
    @GetMapping("/findUrl/{name}")
    public String getModuleUrlByName(@PathVariable("name") String name){
        return moduleService.findModuleUrlByName(name);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void deleteModule(@PathVariable("id") Long id){
         moduleService.deleteModule(id);
    }
}
