package com.cbo.sso.services;

import com.cbo.sso.models.EWSSimpleSend;
import lombok.Getter;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;

@Service
public class ExchangeServiceWrapper {

    @Value("${ews.url}") String ewsUrl;
    @Value("${ews.username}") String ewsUsername;
    @Value("${ews.password}") String ewsPassword;

    @Autowired
    UserDetailService userDetailService;

    @Autowired
    UserService userService;

    private ExchangeService authenticatedExchangeService;
    @Getter
    private boolean authenticationSuccess;
String emailAddressForLg="";
    public boolean authenticateWithHardcodedCredentials() {
        try {
            authenticatedExchangeService = createExchangeServiceWithHardcodedCredentials();
            authenticationSuccess = true;
            return true;
        } catch (Exception e) {
            // Log the exception or handle it accordingly
            authenticationSuccess = false;
            return false;
        }
    }

    public boolean authenticateWithUserCredentials(String username, String password) {
        try {
            authenticatedExchangeService = createExchangeServiceWithUserCredentials(username, password);
            authenticationSuccess = true;
            return true;
        } catch (Exception e) {
            // Log the exception or handle it accordingly
            authenticationSuccess = false;
            return false;
        }
    }

    public ExchangeService getAuthenticatedExchangeService() {
        if (authenticationSuccess) {
            return authenticatedExchangeService;
        } else {
            // Optionally log or throw an exception indicating that the ExchangeService is not authenticated
            return null;
        }
    }


    private ExchangeService createExchangeServiceWithHardcodedCredentials() throws Exception {
        ExchangeService exchangeService = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
        exchangeService.setUrl(new URI(ewsUrl));
        exchangeService.setCredentials(new WebCredentials(ewsUsername, ewsPassword));
        return exchangeService;
    }

    private ExchangeService createExchangeServiceWithUserCredentials(String username, String password) throws Exception {
        ExchangeService exchangeService = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
        exchangeService.setUrl(new URI(ewsUrl));
        exchangeService.setCredentials(new WebCredentials(username, password));
        return exchangeService;
    }

    public void sendEmail(EWSSimpleSend ewsSimpleSend) throws Exception {
        if (!authenticationSuccess) {

            // Optionally log or throw an exception indicating that authentication is required before sending an email
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed. Unable to send email.");
            return;
        }
        try{
            EmailMessage message = new EmailMessage(authenticatedExchangeService);
            message.setSubject(ewsSimpleSend.getSubject());
            message.setBody(MessageBody.getMessageBodyFromText(ewsSimpleSend.getBody()));

            if(ewsSimpleSend.getEmail()[0].contains("@")){
                for (String email : ewsSimpleSend.getEmail()) {
                    message.getToRecipients().add(email);
                    emailAddressForLg = email;

                }
                System.out.println("emaill detected");
            } else {
                for (String id: ewsSimpleSend.getEmail()){
                    emailAddressForLg =userDetailService.getDetail(userService.findUserById(Long.valueOf(id)).getUsername()).get(0).getMail();
                    message.getToRecipients().add(emailAddressForLg);
                    System.out.println("added " +  emailAddressForLg);

                }
                System.out.println("id detected");
            }


            message.send();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("informations/mailLogs.txt", true))) {
                writer.write("Email Sent to " + emailAddressForLg + " at " + LocalDate.now() + " about " + message.getBody()) ;
                writer.newLine();
            } catch (IOException e) {
                System.out.println( "Writing to mailLogs.txt failed");
            }
            ResponseEntity.ok("Email sent successfully.");
        } catch (Exception e){
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();

            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending email: " + e.getMessage());
        }


    }
}
