package com.cbo.sso.repositories;

import com.cbo.sso.models.OrganizationalUnit;
import com.cbo.sso.models.SubProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrganizationalUnitRepository extends JpaRepository<OrganizationalUnit, Long> {
    Optional<OrganizationalUnit> findOrganizationalUnitById(Long id);


    List<OrganizationalUnit> findOrganizationalUnitBySubProcess(SubProcess subProcess);

    @Query("SELECT b FROM OrganizationalUnit b WHERE b.subProcess = (:subProcess)")
    List<OrganizationalUnit> findAllBySubProcess(SubProcess subProcess);

    Optional <OrganizationalUnit> findOrganizationalUnitByName(String name);
}
