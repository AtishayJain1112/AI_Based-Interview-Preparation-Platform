package com.springSecurity.basicOth.Enums;

public enum MailType {

    WELCOME("Welcome to AI Prep", "welcome.ftlh"),
    MODIFY("Your data in AI Prep has been modified", "modify.ftlh"),
    EXIT("Thanks for visiting AI Prep", "exit.ftlh");

    private String subject;
    private String templateFileName;

    MailType(String subject, String templateFileName) {
        this.subject = subject;
        this.templateFileName = templateFileName;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getTemplateFileName() {
        return this.templateFileName;
    }
}
