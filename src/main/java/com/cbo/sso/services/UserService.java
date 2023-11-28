package com.cbo.sso.services;

import com.cbo.sso.exceptions.UserAlreadyExistsException;
import com.cbo.sso.exceptions.UserNotFoundException;
import com.cbo.sso.models.User;
import com.cbo.sso.repositories.UserRepository;
import com.cbo.sso.utility.FileUploadUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
//    private final EmployeeRepository employeeRepository;


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
            throw new UserAlreadyExistsException("Division already exists!");

    if (existingUser != null)
        throw new UserAlreadyExistsException("Division already exists!");
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


    public List<User> findAllUser(){
        return userRepository.findAll();
    }

    public User changeUserOTP(User user){
        return userRepository.save(user);
    }
    public User findUserById(Long id){
        return userRepository.findUserById(id)
                .orElseThrow(()-> new UserNotFoundException("User by id = " + id + " was not found"));
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
