package com.project.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.UserDTO;
import com.project.model.UserForm;
import com.project.repo.UserRepo;
import com.project.service.FileUnzipService;
import com.project.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private  UserRepo userRepository;
    
    @Autowired
    private FileUnzipService fileUnzipService;

//    @PostMapping("/register")
//    public ResponseEntity<Map<String, String>> createUser(
//            @RequestParam("name") String name,
//            @RequestParam("email") String email,
//            @RequestParam("password") String password,
//            @RequestParam("phone") String phone,
//            @RequestParam("dob") String dob,
//            @RequestParam("gender") String gender,
//            @RequestParam("hobbies") String hobbies,
//            @RequestParam("address") String address,
//            @RequestParam("state") String state,
//            @RequestParam("taluka") String taluka,
//            @RequestParam("district") String district,
//            @RequestParam("village") String village,
//            @RequestParam("file") MultipartFile file) 
//    {
//
//        System.out.println("===== Received User Registration Request =====");
//        System.out.println("Name: " + name);
//        System.out.println("Email: " + email);
//
//        // Define base upload directory
//        String baseUploadDir = "D:/zipfiles/";
//        File baseDirectory = new File(baseUploadDir);
//        if (!baseDirectory.exists()) {
//            baseDirectory.mkdirs();
//        }
//
//        // Generate unique folder name for this user
//        String uniqueFolderName = "user_" + System.currentTimeMillis(); // Unique folder
//        String userUploadDir = baseUploadDir + uniqueFolderName + "/";
//        
//        File userDirectory = new File(userUploadDir);
//        if (!userDirectory.exists()) {
//            userDirectory.mkdirs();
//        }
//
//        // Define the file path for storing the ZIP file
//        String zipFileName = "upload_" + System.currentTimeMillis() + ".zip";
//
//        String zipFilePath = userUploadDir + zipFileName;
//        File destinationFile = new File(zipFilePath);
//
//        try {
//            // Save the ZIP file in the user's unique folder
//            file.transferTo(destinationFile);
//            System.out.println("ZIP file saved at: " + zipFilePath);
//        } catch (Exception e) {
//            System.out.println("Error saving file: " + e.getMessage());
//            Map<String, String> errorResponse = new HashMap<>();
//            errorResponse.put("message", "Error saving file: " + e.getMessage());
//            return ResponseEntity.status(500).body(errorResponse);
//        }
//
//        // **Unzip the file in the same folder**
//        String extractedFolderPath = fileUnzipService.unzipFile(zipFilePath, userUploadDir);
//        if (extractedFolderPath == null) {
//            Map<String, String> errorResponse = new HashMap<>();
//            errorResponse.put("message", "Error extracting ZIP file.");
//            return ResponseEntity.status(500).body(errorResponse);
//        }
//
//        // Save user data into the database
//        UserForm userForm = new UserForm();
//        userForm.setName(name);
//        userForm.setEmail(email);
//        userForm.setPassword(password);
//        userForm.setPhone(phone);
//        userForm.setDob(dob);
//        userForm.setGender(gender);
//        userForm.setHobbies(hobbies);
//        userForm.setAddress(address);
//        userForm.setState(state);
//        userForm.setDistrict(district);
//        userForm.setTaluka(taluka);
//        userForm.setVillage(village);
//        userForm.setFilePath(zipFilePath);  // Store ZIP file path
//        userForm.setExtractedFolderPath(extractedFolderPath); // Store extracted folder path
//
//        userRepository.save(userForm);
//
//        Map<String, String> response = new HashMap<>();
//        response.put("message", "User registered successfully");
//        response.put("uploadedZipPath", zipFilePath);
//        response.put("extractedFolderPath", extractedFolderPath);
//
//        return ResponseEntity.ok(response);
//    }
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> createUser(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("phone") String phone,
            @RequestParam("dob") String dob,
            @RequestParam("gender") String gender,
            @RequestParam("hobbies") String hobbies,
            @RequestParam("address") String address,
            @RequestParam("state") String state,
            @RequestParam("taluka") String taluka,
            @RequestParam("district") String district,
            @RequestParam("village") String village,
            @RequestParam("file") MultipartFile file) {

        System.out.println("===== Received User Registration Request =====");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);

        String baseUploadDir = "D:/zipfiles/";
        File baseDirectory = new File(baseUploadDir);
        if (!baseDirectory.exists()) {
            baseDirectory.mkdirs();
        }

        // Unique folder name (for unzipped folder)
        String uniqueFolderName = "unzipped_" + System.currentTimeMillis();

        // Unique ZIP file name
        String zipFileName = "upload_" + System.currentTimeMillis() + ".zip";

        // User directory (extracted folder name will match uniqueFolderName)
        String userUploadDir = baseUploadDir + uniqueFolderName + "/";
        File userDirectory = new File(userUploadDir);
        if (!userDirectory.exists()) {
            userDirectory.mkdirs();
        }

        String zipFilePath = userUploadDir + zipFileName;
        File destinationFile = new File(zipFilePath);

        try {
            file.transferTo(destinationFile);
            System.out.println("ZIP file saved at: " + zipFilePath);
        } catch (Exception e) {
            System.out.println("Error saving file: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error saving file: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }

        // Unzip the file
        String extractedFolderPath = fileUnzipService.unzipFile(zipFilePath, userUploadDir);
        if (extractedFolderPath == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error extracting ZIP file.");
            return ResponseEntity.status(500).body(errorResponse);
        }

        // Save user data into the database
        UserForm userForm = new UserForm();
        userForm.setName(name);
        userForm.setEmail(email);
        userForm.setPassword(password);
        userForm.setPhone(phone);
        userForm.setDob(dob);
        userForm.setGender(gender);
        userForm.setHobbies(hobbies);
        userForm.setAddress(address);
        userForm.setState(state);
        userForm.setDistrict(district);
        userForm.setTaluka(taluka);
        userForm.setVillage(village);
        userForm.setFilePath(zipFileName);            // Store only ZIP file name
        userForm.setExtractedFolderPath(uniqueFolderName); // Store only extracted folder name

        userRepository.save(userForm);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("uploadedZipFileName", zipFileName);
        response.put("extractedFolderName", uniqueFolderName);

        return ResponseEntity.ok(response);
    }

    
    // Get All Users
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() 
    {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Get User by ID
    @GetMapping("/{id}")  
    public ResponseEntity<UserForm> getUserById(@PathVariable Long id) 
    {
        Optional<UserForm> user = userService.getUserById(id);
        if (user.isPresent()) {
            System.out.println("Returning User Data: " + user.get()); // Log before sending response
            return ResponseEntity.ok(user.get());
        } else {
            System.out.println("User not found with ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("phone") String phone,
            @RequestParam("dob") String dob,
            @RequestParam("gender") String gender,
            @RequestParam("address") String address,
            @RequestParam("state") String state,
            @RequestParam("taluka") String taluka,
            @RequestParam("district") String district,
            @RequestParam("village") String village,
            @RequestParam(value = "hobbies", required = false) String hobbies) {

        System.out.println("===== Updating User ID: " + id + " =====");
        System.out.println("Received Name: " + name);
        System.out.println("Received Email: " + email);
        System.out.println("Received Hobbies: " + hobbies);

        // Fetch user from the database
        UserForm user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Update user details
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setDob(dob);
        user.setGender(gender);
//        if (hobbies != null && !hobbies.trim().isEmpty()) 
//        {
//            user.setHobbies(hobbies.replaceAll(",+", ",")); // Remove duplicate commas
//        }
        
        if (hobbies != null && !hobbies.trim().isEmpty()) {
            user.setHobbies(hobbies.replaceAll(",+", ",").replaceAll("^,|,$", "")); // Remove duplicate & leading/trailing commas		
        }
        user.setAddress(address);
        user.setState(state);
        user.setDistrict(district);
        user.setTaluka(taluka);
        user.setVillage(village);

        // Save updated user
        UserForm updatedUser = userRepository.save(user);
        System.out.println("Updated User in DB: " + updatedUser);

        return ResponseEntity.ok(updatedUser);
    }

    // Delete User
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) 
    {
        userService.deleteUser(id);
    }
}





//@PutMapping("/update/{id}")
//public ResponseEntity<?> updateUser(
//      @PathVariable Long id,
//      @RequestParam("name") String name,
//      @RequestParam("email") String email,
//      @RequestParam("password") String password,
//      @RequestParam("phone") String phone,
//      @RequestParam("dob") String dob,
//      @RequestParam("gender") String gender,
//      @RequestParam("address") String address,
//      @RequestParam("state") String state,
//      @RequestParam("taluka") String taluka,
//      @RequestParam("district") String district,
//      @RequestParam("village") String village,
//
//      @RequestParam(value = "hobbies", required = false) String hobbies,
//      @RequestParam(value = "file", required = false) MultipartFile file) {
//
//  System.out.println("===== Updating User ID: " + id + " =====");
//  System.out.println("Received Name: " + name);
//  System.out.println("Received Email: " + email);
//  System.out.println("Received Hobbies: " + hobbies);
//  
//  // Fetch user from the database
//  UserForm user = userRepository.findById(id)
//          .orElseThrow(() -> new RuntimeException("User not found"));
//
//  // Update user details
//  user.setName(name);
//  user.setEmail(email);
//  user.setPassword(password);
//  user.setPhone(phone);
//  user.setDob(dob);
//  user.setGender(gender);
//  user.setHobbies(hobbies != null ? hobbies : user.getHobbies());  // Preserve old hobbies if null
//  user.setAddress(address);
//  user.setState(state);
//  user.setDistrict(district);
//  user.setTaluka(taluka);
//  user.setVillage(village);
//
//  // Handle file upload in D:/uploads/
//  if (file != null && !file.isEmpty()) {
//      try {
//          String uploadDir = "D:/uploads/";
//          File directory = new File(uploadDir);
//          if (!directory.exists()) {
//              directory.mkdirs();
//          }
//
//          String filePath = uploadDir + file.getOriginalFilename();
//          File destinationFile = new File(filePath);
//          file.transferTo(destinationFile);
//          System.out.println("File saved at: " + filePath);
//
//          // Unzip file
//          String extractedFolderPath = fileUnzipService.unzipFile(filePath);
//          if (extractedFolderPath != null) {
//              user.setFilePath(extractedFolderPath);
//          } else {
//              System.out.println("Error extracting ZIP file.");
//          }
//
//      } catch (Exception e) {
//          System.out.println("File upload failed: " + e.getMessage());
//      }
//  }
//
//  // Save updated user
//  UserForm updatedUser = userRepository.save(user);
//  System.out.println("Updated User in DB: " + updatedUser);
//
//  return ResponseEntity.ok(updatedUser);
//}  




//@PostMapping("/register")
//public ResponseEntity<Map<String, String>> createUser(
//  @RequestParam("name") String name,
//  @RequestParam("email") String email,
//  @RequestParam("password") String password,
//  @RequestParam("phone") String phone,
//  @RequestParam("dob") String dob,
//  @RequestParam("gender") String gender,
//  @RequestParam("hobbies") String hobbies,
//  @RequestParam("address") String address,
//  @RequestParam("state") String state,
//  @RequestParam("taluka") String taluka,
//  @RequestParam("district") String district,
//  @RequestParam("village") String village,
//
//  @RequestParam("file") MultipartFile file) {
//  
//  System.out.println("===== Received User Registration Request =====");
//  System.out.println("Name: " + name);
//  System.out.println("Email: " + email);
//  System.out.println("File Name: " + file.getOriginalFilename());
//
//  // Define the directory path
//  String uploadDir = "D:/uploads/";
//  File directory = new File(uploadDir);
//  if (!directory.exists()) {
//      directory.mkdirs();  // Create directory if it doesn't exist
//  }
//
//  // Define the file path
//  String filePath = uploadDir + file.getOriginalFilename();
//  File destinationFile = new File(filePath);
//
//  try 
//  {
//      // Save the file to the specified directory
//      file.transferTo(destinationFile);
//      System.out.println("File saved at: " + filePath);
//  }
//  catch (Exception e) 
//  {
//      System.out.println("Error saving file: " + e.getMessage());
//      Map<String, String> errorResponse = new HashMap<>();
//      errorResponse.put("message", "Error saving file: " + e.getMessage());
//      return ResponseEntity.status(500).body(errorResponse);
//  }
//
//  // **Unzip the file automatically**
//  String extractedFolderPath = fileUnzipService.unzipFile(filePath);
//  if (extractedFolderPath == null) {
//      Map<String, String> errorResponse = new HashMap<>();
//      errorResponse.put("message", "Error extracting ZIP file.");
//      return ResponseEntity.status(500).body(errorResponse);
//  }
//
//  // Save user to the database with extracted file path
//  UserForm userForm = new UserForm();
//  userForm.setName(name);
//  userForm.setEmail(email);
//  userForm.setPassword(password);
//  userForm.setPhone(phone);
//  userForm.setDob(dob);
//  userForm.setGender(gender);
//  userForm.setHobbies(hobbies);
//  userForm.setAddress(address);
//  userForm.setFilePath(filePath); // Save ZIP filename
//  userForm.setState(state);
//  userForm.setDistrict(district);
//  userForm.setTaluka(taluka);
//  userForm.setVillage(village);
//  
//  userRepository.save(userForm);
//  
//  Map<String, String> response = new HashMap<>();
//  response.put("message", "User registered successfully");
//  response.put("extractedFolderPath", extractedFolderPath);
//
//  return ResponseEntity.ok(response);
//}









