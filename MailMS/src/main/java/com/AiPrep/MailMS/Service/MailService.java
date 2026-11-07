package com.AiPrep.MailMS.Service;

import com.AiPrep.MailMS.DTO.MailDTO;
import org.springframework.core.io.Resource;


public interface MailService {
    String sendAdvancedMail(MailDTO mailDTO);
}
