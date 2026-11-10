package com.springSecurity.basicOth.DTO;

import com.springSecurity.basicOth.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    public String username;
    public String email;
    public List<Role> role;
}
