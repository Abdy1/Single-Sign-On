package com.cbo.sso.controllers;

import com.cbo.sso.models.*;
//import com.cbo.sso.services.OrganizationalUnitService;
import com.cbo.sso.repositories.ModuleRepository;
import com.cbo.sso.services.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/user")

public class UserController {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModuleRepository moduleRepository;
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

    @GetMapping("/signatureImagePath/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'EMS_ADMIN', 'CC_ADMIN', 'ICMS_ADMIN', 'SASV_ADMIN', 'MEMO_ADMIN', 'ECX_ADMIN', 'CMS_ADMIN' )")
    public ResponseEntity<byte[]> getEmployeeSignatureImage(@PathVariable long id) {
        System.out.println("Inside getEmployeeSignatureImage = employee"+id);
        User employee = userService.findUserById(id);
        String imagePath = employee.getSignatureImage();
        System.out.println("signature_image = " + imagePath);
        String imageFilePath = "./user-photos/employee/"+id+"/"+imagePath;
        try {
            System.out.println(imageFilePath);
            byte[] imageBytes = Files.readAllBytes(Paths.get(imageFilePath));
            System.out.println(imageBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(imageBytes.length);
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<User> getUserId (@PathVariable("id") Long id) {
        User user= userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);

    }
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('SYSTEM_ADMIN')")
    public ResponseEntity<User> addUser(@RequestBody
                                        @RequestParam(name="employeeImage", required = false) MultipartFile employeeImage,
                                        @RequestParam(name="signatureImage", required = false) MultipartFile signatureImage,
                                        @RequestParam(name="username") String username,
                                        @RequestParam(name="active") String active,
                                        @RequestParam(name="id") String id,
                                        @RequestParam(name="roles", required = false) MultipartFile rolesFile
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Set<Role> roles = new HashSet<>();
        if (rolesFile != null) {
            roles = objectMapper.readValue(rolesFile.getBytes(), new TypeReference<Set<Role>>(){});
        }

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


    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','SYSTEM_ADMIN' ,'EMS_ADMIN', 'CC_ADMIN', 'ICMS_ADMIN', 'SASV_ADMIN', 'MEMO_ADMIN', 'ECX_ADMIN', 'CMS_ADMIN' )")
    public ResponseEntity<User> updateUser(@RequestBody
                                           @RequestParam(name="username") String username,
                                           @RequestPart("roles") MultipartFile rolesFile
    ) throws IOException {

        User newUser = userService.updateUser(username, rolesFile);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(location).body(newUser);
    }

    @PostMapping("/update/ad")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','SYSTEM_ADMIN' ,'EMS_ADMIN', 'CC_ADMIN', 'ICMS_ADMIN', 'SASV_ADMIN', 'MEMO_ADMIN', 'ECX_ADMIN', 'CMS_ADMIN' )")
    public ResponseEntity<User> updateUser(@RequestBody User user
    ) throws IOException {

        User newUser = userService.changeAD(String.valueOf(user.getId()), user.getUsername());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(location).body(newUser);
    }


    @PostMapping("/delete-role")
    public ResponseEntity<?> deleteUserRole(@RequestBody Map<String, Long> request) {
        try {
            long userId = request.get("user_id");
            long roleId = request.get("role_id");
            userService.deleteUserRole(userId, roleId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting user role.");
        }
    }





    public ResponseEntity<User> addUserAutomation(String username, String id, Set<Role> roles) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        User user = new User();
        user.setUsername(username);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setId(Long.parseLong(id));
        user.setActive(true);
        user.setCreatedAt(new Date().toLocaleString());
        user.setUpdatedAt(new Date().toLocaleString());
        user.setRoles(roles);

        User newUser = userService.addUser2(user, null, null);
        return null;
    }

    @PostMapping("/automateUserCreation")
    public void handleADFileUpload(@RequestParam("file") MultipartFile multipartFile) {
        Set<Role> roles = new HashSet<>();

        roles.add(new Role(13L, ERole.ROLE_SASV_USER, 12L, moduleRepository.findModuleById(5L).get()));
        if (!multipartFile.isEmpty()) {
            try(InputStreamReader reader = new InputStreamReader(multipartFile.getInputStream())) {
                CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);
                Iterator<CSVRecord> recordIterator = csvParser.iterator();
                // Iterate over CSV records starting from the third record
                while (recordIterator.hasNext()) {
                    CSVRecord csvRecord = recordIterator.next();

                    // Process each CSV record as needed
                    String adid = csvRecord.get(1);
                    String empid = csvRecord.get(2);

                    System.out.println(adid + "ad id");
                    System.out.println(empid + "empid id");



                    if(idNotExist(empid)){
                        System.out.println(empid + "don't exist creating new one");
                        // Check if "id" is not empty before adding data

                        // Call your store method or perform other actions

                        addUserAutomation(adid,empid,roles);
                        System.out.println("username " +adid + "id" + empid + "role" + "DEFAULT");

                    }





                }


            } catch (Exception e) {
                System.out.println("CSV processing error: " + e.getMessage());
            }
        }


    }

    private boolean idNotExist(String id){
        User user = userService.findUserById(Long.valueOf(id));

        if(user != null){
            System.out.println(user.getUsername());
            System.out.println("User already exists");
            return false; // User exists
        } else {
            System.out.println("User doesn't exist");
            return true; // User does not exist
        }
    }

}


