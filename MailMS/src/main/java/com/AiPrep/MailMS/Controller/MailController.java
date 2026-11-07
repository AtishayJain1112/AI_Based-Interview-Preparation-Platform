package com.AiPrep.MailMS.Controller;

import com.AiPrep.MailMS.DTO.MailDTO;
import com.AiPrep.MailMS.Service.MailService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {
    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/mail")
    public ResponseEntity<String> sendAdvancedMail(@RequestBody MailDTO mailDTO) {
        String response = mailService.sendAdvancedMail(mailDTO);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
