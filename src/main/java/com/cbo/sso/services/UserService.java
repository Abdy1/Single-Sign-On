package com.cbo.sso.services;

import com.cbo.sso.exceptions.UserAlreadyExistsException;
import com.cbo.sso.exceptions.UserNotFoundException;
import com.cbo.sso.models.Role;
import com.cbo.sso.models.User;
import com.cbo.sso.repositories.UserRepository;
import com.cbo.sso.utility.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;



import java.io.IOException;
import java.util.List;

//check the ones i need
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


@Service

public class UserService {
    private final UserRepository userRepository;
//    private final EmployeeRepository employeeRepository;
  User theOne;

    @Autowired
    private ObjectMapper objectMapper;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
public User addUser(User user, MultipartFile employeeImg, MultipartFile signatureImg) throws IOException {
    User savedU = new User();
    System.out.println("user = "+user);
    System.out.println("empimg = "+employeeImg);
    User existingUser = null;
    if(user.getId() != null){
        existingUser = userRepository.findById(user.getId()).orElse(null);

    }
    User userByName = userRepository.findByUsername(user.getUsername());

    if(userByName != null)
            throw new UserAlreadyExistsException("User already exists!");

    if (existingUser != null)
        throw new UserAlreadyExistsException("User already exists!");
    else {
        System.out.println("employeeImg = "+employeeImg);
        if(employeeImg != null){
            savedU = userRepository.save(user);
            String uploadDir = "user-photos/employee/" + savedU.getId();
            System.out.println("  upload = "+uploadDir);
            FileUploadUtil.saveFile(uploadDir, user.getEmployeeImage(), employeeImg);
            FileUploadUtil.saveFile(uploadDir, user.getSignatureImage(), signatureImg);
            return savedU;
        }else{
            savedU = userRepository.save(user);
            return savedU;
        }
    }
}
public User updateUser(String username, MultipartFile rolesFile) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    Set<Role> roles = objectMapper.readValue(rolesFile.getBytes(), new TypeReference<Set<Role>>(){});
    User user = userRepository.findByUsername(username);
    Set<Role> allRoles = user.getRoles();
    allRoles.addAll(roles);
    user.setRoles(allRoles);
    userRepository.save(user);
    return user;
    }

    public User deleteUserRole(Long user_id, Long role_id) throws IOException {
        User user = userRepository.findUserById(user_id).get();

            Set<Role> roles = user.getRoles();
            Iterator<Role> iterator = roles.iterator();
            while(iterator.hasNext()){
                Role role = iterator.next();
                if(role.getId().equals(role_id)){
                    iterator.remove();
                    break;
                }
            }
            userRepository.save(user);


        return user;
    }

    public User addUser2(User user, MultipartFile employeeImg, MultipartFile signatureImg) throws IOException {
        User savedU = new User();
        System.out.println("user = "+user);
        System.out.println("empimg = "+employeeImg);
        User existingUser = null;
        if(user.getId() != null){
            existingUser = userRepository.findById(user.getId()).orElse(null);

        }
        User userByName = userRepository.findByUsername(user.getUsername());

        if(userByName != null){
            System.out.println("User already exists!");
            return null;
        }

        if (existingUser != null){
            System.out.println("user already exist");
            return null;
        } else {
            System.out.println("employeeImg = "+employeeImg);
            if(employeeImg != null){
                savedU = userRepository.save(user);
                String uploadDir = "user-photos/employee/" + savedU.getId();
                System.out.println("  upload = "+uploadDir);
                FileUploadUtil.saveFile(uploadDir, user.getEmployeeImage(), employeeImg);
                FileUploadUtil.saveFile(uploadDir, user.getSignatureImage(), signatureImg);
                return savedU;
            }else{
                savedU = userRepository.save(user);
                return savedU;
            }
        }
    }


    public List<User> findAllUser(){
        return userRepository.findAll();
    }

    public User changeUserOTP(User user){
        return userRepository.save(user);
    }
    public User findUserById(Long id) {
        try {
            return userRepository.findUserById(id).orElse(null);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    //public User findUserByEmployee(Employee employee) { return userRepository.findByEmployee(employee);}
    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public User getLastUser() {
        return userRepository.findFirstByOrderByIdDesc();
    }



}
