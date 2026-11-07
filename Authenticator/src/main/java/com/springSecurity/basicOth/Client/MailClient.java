package com.springSecurity.basicOth.Client;

import com.springSecurity.basicOth.DTO.MailDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "MailMS",url = "${mail.service.url}")
public interface MailClient {

    @PostMapping("/mail")
    String sendEmail(@RequestBody MailDTO mailDTO);

}
