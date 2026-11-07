package com.AiPrep.MailMS.Service;

import com.AiPrep.MailMS.DTO.MailDTO;

import com.AiPrep.MailMS.Enums.MailType;
import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final Configuration templateConfig;
    @Value("${spring.mail.username}")
    private String senderEmail;
    @Value("${success.message}")
    private String successMessage;
    @Value("${failure.message}")
    private String failureMessage;


    public MailServiceImpl(JavaMailSender mailSender, Configuration templateConfig) {
        this.mailSender = mailSender;
        this.templateConfig = templateConfig;
    }


    @Override
    public String sendAdvancedMail(MailDTO mailDTO) {
        return sendEmailWithAttachment(mailDTO);
    }
    private String sendEmailWithAttachment(MailDTO mailDTO) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setFrom(senderEmail);
            mimeMessageHelper.setTo(mailDTO.to());
            mimeMessageHelper.setSubject(mailDTO.category().getSubject());
            mimeMessageHelper.setText(buildMailBodyWithTemplate(mailDTO.category(), mailDTO.firstName().concat(" ").concat(mailDTO.lastName())), true);
            mailSender.send(message);
            return String.format(successMessage, mailDTO.to(), LocalDateTime.now());
        } catch (Exception e) {
            e.printStackTrace();
            return String.format(failureMessage, mailDTO.to(), LocalDateTime.now());
        }
    }
    private String buildMailBodyWithTemplate(MailType category, String ownerName) {
        Map<String, String> dataModel = new HashMap<>();
        dataModel.put("ownerName", ownerName);
        Writer writer = new StringWriter();
        try {
            templateConfig.getTemplate(category.getTemplateFileName()).process(dataModel, writer);
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return ownerName;
        }
    }
}
