
//package com.cbo.sso.controllers;
//
//import com.cbo.sso.models.OrganizationalUnit;
////import com.cbo.sso.services.OrganizationalUnitService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("organizationalUnit")
//public class OrganizationalUnitController {
//
////        private final OrganizationalUnitService organizationalUnitService;
//
//        public OrganizationalUnitController(OrganizationalUnitService organizationalUnitService) {
//            this.organizationalUnitService = organizationalUnitService;
//        }
//        @GetMapping("/all")
//        @PreAuthorize("hasAnyRole('ICMS_ADMIN','ICMS_BRANCH','ICMS_DISTRICT','ICMS_BRANCH_MANAGER', 'ICMS_PROVISION')")
//        public ResponseEntity<List<OrganizationalUnit>> getAllOrganizationalUnites(){
//            List<OrganizationalUnit> organizationalUnits = organizationalUnitService.findAllOrganizationalUnit();
//            return new ResponseEntity<>(organizationalUnits, HttpStatus.OK);
//        }
//        @GetMapping("/find/{id}")
//        @PreAuthorize("hasAnyRole('ICMS_ADMIN','ICMS_BRANCH','ICMS_DISTRICT','ICMS_BRANCH_MANAGER', 'ICMS_PROVISION')")
//        public ResponseEntity<OrganizationalUnit> getOrganizationalUnitId (@PathVariable("id") Long id) {
//           OrganizationalUnit organizationalUnit= organizationalUnitService.findOrganizationalUnitById(id);
//            return new ResponseEntity<>(organizationalUnit, HttpStatus.OK);
//        }
//    }
//
//
