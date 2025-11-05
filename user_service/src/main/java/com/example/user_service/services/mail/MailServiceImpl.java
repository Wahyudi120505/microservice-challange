package com.example.user_service.services.mail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private SendGrid sendGrid;

    @Autowired
    private ThymeleafService thymeleafService;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    @Value("${sendgrid.from.name}")
    private String fromName;

    @Override
    public void emailRegistration(String to, String name) {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("name", name);
            String htmlContent = thymeleafService.createContext("registration.html", variables);

            Email from = new Email(fromEmail, fromName);
            Email toEmail = new Email(to);
            String subject = "Registration New User";
            Content content = new Content("text/html", htmlContent);
            Mail mail = new Mail(from, subject, toEmail, content);

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);

            System.out.println("SendGrid response status: " + response.getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
