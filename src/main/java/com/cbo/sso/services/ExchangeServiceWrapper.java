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
String username = ewsSimpleSend.getEmail()[0].split("\\.")[0].substring(0,1).toUpperCase() + ewsSimpleSend.getEmail()[0].split("\\.")[0].substring(1) + " " + ewsSimpleSend.getEmail()[0].split("\\.")[1].split("@")[0].substring(0,1).toUpperCase() +ewsSimpleSend.getEmail()[0].split("\\.")[1].split("@")[0].substring(1);
            String emailBody = String.format(
                    "<!DOCTYPE html>\n" +
                            "<html lang=\"en\">\n" +
                            "<head>\n" +
                            "  <meta charset=\"utf-8\" />\n" +
                            "  <meta name=\"viewport\" content=\"width=device-width,initial-scale=1\" />\n" +
                            "  <title>Cooperative Bank of Oromia — Notification</title>\n" +
                            "</head>\n" +
                            "<body style=\"margin:0;padding:0;background:#f3f6f8;font-family:Arial,Helvetica,sans-serif;-webkit-text-size-adjust:100%%;\">\n" + // note: this line contains the webkit adjustment and must keep '100%' escaped if you use String.format; see note below
                            "  <!-- Preheader (hidden preview text) -->\n" +
                            "  <span style=\"display:none!important;visibility:hidden;opacity:0;height:0;width:0;font-size:1px;color:#ffffff;line-height:1;overflow:hidden;\">Cooperative Bank of Oromia — Important Notification: Please review the message inside.</span>\n" +
                            "  <div style=\"width:100%%;background:#f3f6f8;padding:20px 12px;box-sizing:border-box;\">\n" +
                            "    <div style=\"max-width:620px;margin-left:auto;margin-right:auto;background:#ffffff;border-radius:8px;overflow:hidden;box-shadow:0 4px 10px rgba(0,0,0,0.05);box-sizing:border-box;\">\n" +
                            "      <!-- Header -->\n" +
                            "      <div role=\"banner\" aria-label=\"Cooperative Bank of Oromia Header\" style=\"background-color:#1CD5FF;padding:12px 16px;display:block;color:#ffffff;box-sizing:border-box;\">\n" +
                            "        <div style=\"display:inline-block;vertical-align:middle;margin-left:12px;line-height:1;\">\n" +
                            "          <h1 style=\"margin:0;font-size:16px;color:#ffffff;font-weight:700;\">Cooperative Bank of Oromia</h1>\n" +
                            "          <p style=\"margin:2px 0 0;font-size:12px;color:rgba(255,255,255,0.9);\">Realflow Notification</p>\n" +
                            "        </div>\n" +
                            "      </div>\n" +
                            "      <!-- Content -->\n" +
                            "      <div role=\"main\" style=\"padding:16px;color:#122434;font-size:14px;line-height:1.4;box-sizing:border-box;\">\n" +
                            "        <div style=\"background:#f8fbfc;border:1px solid #e6f2f5;padding:12px;border-radius:6px;margin-bottom:12px;box-sizing:border-box;\">\n" +
                            "          <p style=\"margin:0 0 8px 0;font-weight:600;\">Hello <strong>"+ username +"</strong>,</p>\n" +
                            "          <p style=\"margin:0;color:#22343a;\">" + ewsSimpleSend.getBody() + "</p>\n" +
                            "        </div>\n" +

                            "        <div style=\"margin-top:16px;color:#44595f;font-size:13px;line-height:1.45;\">\n" +
                            "          <strong>Need help?</strong>\n" +
                            "          <p style=\"margin:6px 0 0;\">Call our support center: <a href=\"tel: 609\" style=\"color:#1CD5FF;font-weight:700;text-decoration:none;\"> 609</a> or visit <a href=\"https://www.coopbankoromia.com\" style=\"color:#1CD5FF;font-weight:700;text-decoration:none;\">coopbankoromia.com</a></p>\n" +
                            "          <p style=\"margin:6px 0 0;\">Contact: <a href=\"#\" style=\"color:#1CD5FF;font-weight:700;text-decoration:none;\"> ITSM </a> </p>\n" +

                            "        </div>\n" +
                            "      </div>\n" +
                            "      <!-- Footer -->\n" +
                            "      <div role=\"contentinfo\" style=\"padding:12px 16px;background:transparent;color:#6a7b82;font-size:12px;text-align:center;box-sizing:border-box;\">\n" +
                            "        <div>© 2026 Cooperative Bank of Oromia. All rights reserved.</div>\n" +
                            "        <div style=\"margin-top:6px;color:#7a8a90;font-size:12px;\">Designed by Application Team</div>\n" +
                            "        <div style=\"margin-top:12px;\">\n" +
                            "          <a href=\"https://www.facebook.com/share/1BiexZbHMx/\" title=\"Facebook\" style=\"display:inline-block;width:28px;height:28px;border-radius:4px;background:#ffffff;border:1px solid #e1eef1;line-height:28px;text-align:center;color:#007c91;font-weight:700;text-decoration:none;margin-right:6px;\">f</a>\n" +
                            "          <a href=\"https://www.linkedin.com/in/ephrem-daniel-31966b227/\" title=\"LinkedIn\" style=\"display:inline-block;width:28px;height:28px;border-radius:4px;background:#ffffff;border:1px solid #e1eef1;line-height:28px;text-align:center;color:#007c91;font-weight:700;text-decoration:none;\">in</a>\n" +
                            "        </div>\n" +
                            "      </div>\n" +
                            "    </div>\n" +
                            "  </div>\n" +
                            "</body>\n" +
                            "</html>\";",
                    ewsSimpleSend.getSubject(),
                    ewsSimpleSend.getBody()
            );

            message.setBody(MessageBody.getMessageBodyFromText(emailBody));
//            message.setBody(MessageBody.getMessageBodyFromText(ewsSimpleSend.getBody()));

            if(ewsSimpleSend.getEmail()[0].contains("@")){
                for (String email : ewsSimpleSend.getEmail()) {
                    message.getToRecipients().add(email);
                    emailAddressForLg = email;

                }
                System.out.println("email detected " + emailAddressForLg);
            } else {
                for (String id: ewsSimpleSend.getEmail()){
                    emailAddressForLg =userDetailService.getDetail(userService.findUserById(Long.valueOf(id)).getUsername()).get(0).getMail();
                    message.getToRecipients().add(emailAddressForLg);
                    System.out.println("added " +  emailAddressForLg);

                }
                System.out.println("id detected " + emailAddressForLg);
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
