package com.cbo.sso.controllers;

import com.cbo.sso.models.*;
//import com.cbo.sso.services.OrganizationalUnitService;
import com.cbo.sso.services.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private ObjectMapper objectMapper;
    private final UserService userService;
//    private final OrganizationalUnitService organizationalUnitService;

    public UserController(UserService userService) {
        this.userService = userService;
//        this.organizationalUnitService = organizationalUnitService;
    }
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<User>> getAllusers(){
        List<User> users = userService.findAllUser();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<User> getUserId (@PathVariable("id") Long id) {
        User user= userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'EMS_ADMIN', 'CC_ADMIN', 'ICMS_ADMIN', 'SASV_ADMIN', 'MEMO_ADMIN', 'ECX_ADMIN', 'CMS_ADMIN' )")
    public ResponseEntity<User> addUser(@RequestBody
                                                @RequestParam(name="employeeImage", required = false) MultipartFile employeeImage ,
                                                @RequestParam(name="signatureImage", required = false) MultipartFile signatureImage ,
                                                @RequestParam(name="username") String username,
                                                @RequestParam(name="active") String active,
                                                @RequestPart("roles") MultipartFile rolesFile,
                                                @RequestParam(name="id") String id
                                        ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Set<Role> roles = objectMapper.readValue(rolesFile.getBytes(), new TypeReference<Set<Role>>(){});
        User user = new User();
        user.setUsername(username);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setId(Long.parseLong(id));
        user.setActive(Boolean.parseBoolean(active));
        user.setCreatedAt(new Date().toLocaleString());
        user.setUpdatedAt(new Date().toLocaleString());
        user.setRoles(roles);
        if (employeeImage != null){
            String employeeImg = StringUtils.cleanPath(employeeImage.getOriginalFilename());
            user.setEmployeeImage(employeeImg);
        }
        if(signatureImage != null){
            String signatureImg = StringUtils.cleanPath(signatureImage.getOriginalFilename());
            user.setSignatureImage(signatureImg);
        }
        User newUser = userService.addUser(user, employeeImage, signatureImage);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(location).body(user);
    }
}


