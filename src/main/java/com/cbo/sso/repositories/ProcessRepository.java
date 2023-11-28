package com.cbo.sso.repositories;
import com.cbo.sso.models.Process;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessRepository extends JpaRepository<Process, Long> {
}
