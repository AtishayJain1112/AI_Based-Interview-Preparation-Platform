package com.springSecurity.basicOth.DTO;


import com.springSecurity.basicOth.Enums.MailType;

public record MailDTO(
        String to, String firstName, String lastName, MailType category
){}
