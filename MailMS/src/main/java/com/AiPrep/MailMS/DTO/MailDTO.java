package com.AiPrep.MailMS.DTO;

import com.AiPrep.MailMS.Enums.MailType;

public record MailDTO (
        String to, String firstName, String lastName, MailType category
){}
