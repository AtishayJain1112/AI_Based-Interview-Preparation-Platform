package com.springSecurity.basicOth.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponseDTO {
    String Username;
    String email;
    String message;
}
