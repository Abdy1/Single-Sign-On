//package com.cbo.sso.services;
//
//import com.cbo.sso.exceptions.UserNotFoundException;
//import com.cbo.sso.models.*;
//import com.cbo.sso.models.Process;
//import com.cbo.sso.repositories.*;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class OrganizationalUnitService {
//    private EmployeeRepository employeeRepository;
//    private ProcessRepository processRepository;
//    private WorkCenterRepository workCenterRepository;
//    private SubProcessRepository subProcessRepository;
//    private OrganizationalUnitRepository organizationalUnitRepository;
//
//    public OrganizationalUnitService(
//                          EmployeeRepository employeeRepository,
//                          ProcessRepository processRepository,
//                          WorkCenterRepository workCenterRepository,
//                          SubProcessRepository subProcessRepository,
//                          OrganizationalUnitRepository organizationalUnitRepository
//    ) {
//        this.employeeRepository = employeeRepository;
//        this.processRepository = processRepository;
//        this.workCenterRepository = workCenterRepository;
//        this.subProcessRepository = subProcessRepository;
//        this.organizationalUnitRepository = organizationalUnitRepository;
//    }
//
//    public void preSave(OrganizationalUnit organizationalUnit) {
//
//        //check process is not saved again
//        Process process = organizationalUnit.getSubProcess().getProcess();
//        if (!processRepository.existsById(process.getId())) {
//            processRepository.save(process);
//        }
//
//        //check workCenter is not saved again
//        WorkCenter workCenter = organizationalUnit.getSubProcess().getWorkCenter();
//        if (!workCenterRepository.existsById(workCenter.getId())) {
//            workCenterRepository.save(workCenter);
//        }
//
//        //check subProcess is not saved again
//        SubProcess subProcess = organizationalUnit.getSubProcess();
//        if (!subProcessRepository.existsById(subProcess.getId())) {
//            subProcessRepository.save(subProcess);
//        }
//
//        //check organizationalUnit is not saved again
//        if (!organizationalUnitRepository.existsById(organizationalUnit.getId())) {
//            organizationalUnitRepository.save(organizationalUnit);
//        }
//    }
//    public OrganizationalUnit addOrganizationalUnit(OrganizationalUnit organizationalUnit){
//
//        return organizationalUnitRepository.save(organizationalUnit);
//    }
//    public List<OrganizationalUnit> findAllOrganizationalUnit(){
//        return organizationalUnitRepository.findAll();
//    }
//    public OrganizationalUnit  updateOrganizationalUnit(OrganizationalUnit organizationalUnit){
//        return organizationalUnitRepository.save(organizationalUnit);
//    }
//    public OrganizationalUnit findOrganizationalUnitById(Long id){
//        return organizationalUnitRepository.findOrganizationalUnitById(id)
//                .orElseThrow(()-> new UserNotFoundException("OrganizationalUnit by id" + id + " was not found"));
//    }
//
//    public List<OrganizationalUnit> findOrganizationalUnitBySubProcess(SubProcess subProcess){
//        return organizationalUnitRepository.findOrganizationalUnitBySubProcess(subProcess);
//    }
//    public String deleteOrganizationalUnit(Long id){
//
//        organizationalUnitRepository.deleteById(id);
//        return  "Record deleted Succesfully";
//    }
//
//    public OrganizationalUnit findOrganizationalUnitByName(String name) {
//        return organizationalUnitRepository.findOrganizationalUnitByName(name).orElseThrow(()-> new UserNotFoundException("OrganizationalUnit by name=" + name + " was not found"));
//    }
//
//}
