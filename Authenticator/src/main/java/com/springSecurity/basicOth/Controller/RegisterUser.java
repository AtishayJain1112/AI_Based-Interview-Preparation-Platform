package com.springSecurity.basicOth.Controller;

import com.springSecurity.basicOth.DTO.RegisterResponseDTO;
import com.springSecurity.basicOth.DTO.RegisterUserDTO;
import com.springSecurity.basicOth.Security.CustomUserDetails;
import com.springSecurity.basicOth.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController

public class RegisterUser {
    private UserService userService;
    public RegisterUser(UserService userService){
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerUser(@RequestBody RegisterUserDTO user){

        RegisterResponseDTO response = userService.saveUser(user);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails){
        boolean isDeleted = userService.deleteUser(userDetails.getId());
        if(isDeleted){
            return ResponseEntity.ok("User deleted successfully");
        }else{
            return ResponseEntity.status(404).body("User not found");
        }
    }

}
