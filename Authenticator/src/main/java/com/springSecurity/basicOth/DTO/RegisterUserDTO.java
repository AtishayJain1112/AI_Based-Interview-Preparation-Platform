package com.springSecurity.basicOth.DTO;

import com.springSecurity.basicOth.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO {
    String  username;
    String email;
    String password;

}
