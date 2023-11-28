package com.cbo.sso.services;

import com.cbo.sso.exceptions.NoSuchUserExistsException;
import com.cbo.sso.exceptions.ResourceNotFoundException;
import com.cbo.sso.models.Module;
import com.cbo.sso.repositories.ModuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {
    private final ModuleRepository moduleRepository;

    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }
    public Module addModule(Module module){

        return moduleRepository.save(module);
    }
    public List<Module> findAllModule(){
        return moduleRepository.findAll();
    }
    public Module updateModule(Module module){
        Module existingModule = moduleRepository.findById(module.getId()).orElse(null);

        if (existingModule == null)
            throw new NoSuchUserExistsException("No such Module exists!");
        else {
            existingModule.setName(module.getName());
            existingModule.setUrl(module.getUrl());
            Module updatedModule = moduleRepository.save(existingModule);
            return updatedModule;
        }
    }
    public Module findModuleById(Long id){
        return moduleRepository.findModuleById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Module by id = " + id + " was not found"));
    }
    public Long findModuleIdByName(String name) {
        return moduleRepository.findModuleByName(name).get().getId();
    }
    public String findModuleUrlByName(String name) {
        return moduleRepository.findModuleByName(name).get().getUrl();
    }
    public void deleteModule(Long id){
        moduleRepository.deleteById(id);
    }
    public Module findModuleByName(String name) {
        return moduleRepository.findModuleByName(name).orElseThrow(()-> new ResourceNotFoundException("Module by name = " + name + " was not found"));
    }
    public Module findModuleByCode(String code) {
        return moduleRepository.findModuleByCode(code);
    }
}