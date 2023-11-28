package com.cbo.sso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cbo.sso.models.Module;
import java.util.Optional;

public interface ModuleRepository extends JpaRepository<Module, Long> {
    Optional<Module> findModuleById(Long id);
    Optional<Module> findModuleByName(String name);
    Module findModuleByCode(String code);
}
