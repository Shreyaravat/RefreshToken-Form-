package com.project.service;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.model.UserDTO;
import com.project.model.UserForm;
import com.project.repo.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    // Create User
//    public UserForm createUser(UserForm user) {
//        return userRepository.save(user);
//    }

    // Get All Users
    public List<UserDTO> getAllUsers() 
    {
    	List<UserForm> users = userRepository.findAll();
    	return users.stream().map(UserDTO :: new).collect(Collectors.toList());
    }

    // Get User by ID
    public Optional<UserForm> getUserById(Long id) {
        return userRepository.findById(id).map(user -> {
            System.out.println("User Data: " + user); // Log the data
            return user;
        });
    }


    // Update User
    public UserForm updateUser(Long id, UserForm userDetails, MultipartFile file) {
    	UserForm user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
        user.setDob(userDetails.getDob());
        user.setGender(userDetails.getGender());
        user.setHobbies(userDetails.getHobbies());
        user.setAddress(userDetails.getAddress());
        user.setState(userDetails.getState());
        user.setDistrict(userDetails.getDistrict());
        user.setTaluka(userDetails.getTaluka());
        user.setVillage(userDetails.getVillage());

        
      return userRepository.save(user);
    }

    // Delete User
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}


//if (file != null && !file.isEmpty()) {
//  try {
//      String filePath = "uploads/" + file.getOriginalFilename(); // Set the storage path
//      file.transferTo(new java.io.File(filePath)); // Save the file
//      user.setFilePath(filePath); // Update file path in the database
//  } catch (Exception e) {
//      throw new RuntimeException("File upload failed", e);
//  }
//} 

//Handle file upload
//
//if (file != null && !file.isEmpty()) {
//  try {
//      // Ensure the uploads directory exists
//      File uploadDir = new File("uploads");
//      if (!uploadDir.exists()) {
//          uploadDir.mkdirs();  // Creates the directory if it doesn't exist
//      }
//
//      // Set file path
//      String filePath = uploadDir.getAbsolutePath() + File.separator + file.getOriginalFilename();
//      File destFile = new File(filePath);
//
//      // Save the file
//      file.transferTo(destFile);
//      user.setFilePath(filePath); // Update file path in the database
//
//  } catch (Exception e) {
//      throw new RuntimeException("File upload failed", e);
//  }
//}
