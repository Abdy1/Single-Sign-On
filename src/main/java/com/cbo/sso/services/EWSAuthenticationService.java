//package com.cbo.sso.services;
//
//import com.cbo.sso.models.EWSSimpleSend;
//import microsoft.exchange.webservices.data.core.ExchangeService;
//import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
//import microsoft.exchange.webservices.data.core.service.folder.Folder;
//import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
//import microsoft.exchange.webservices.data.property.complex.MessageBody;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Arrays;
//
//@Service
//public class EWSAuthenticationService {
//
//    @Autowired
//    private ExchangeService exchangeService;
//
//    public String doEWSOperation() throws Exception {
//        // Example: Get the root folder
//        Folder rootFolder = Folder.bind(exchangeService, WellKnownFolderName.MsgFolderRoot);
//        return "EWS operation successful. Root folder name: " + rootFolder.getDisplayName();
//    }
//    public void testEmail() throws Exception {
//        EmailMessage message = new EmailMessage(exchangeService);
//        message.setSubject("Test Subject");
//        message.setBody(MessageBody.getMessageBodyFromText("Test Body"));
//        message.getToRecipients().add("Abdellah.Kamil@coopbankoromiasc.com");
//        message.send();
//    }
//    public void sendEmail(EWSSimpleSend ewsSimpleSend) throws Exception {
//        EmailMessage message = new EmailMessage(exchangeService);
//        message.setSubject(ewsSimpleSend.getSubject());
//        message.setBody(MessageBody.getMessageBodyFromText(ewsSimpleSend.getBody()));
//        for (String email: ewsSimpleSend.getEmail()) {
//          message.getToRecipients().add(email);
//        }
////        message.getToRecipients().add(Arrays.toString(ewsSimpleSend.getEmail()));
//        message.send();
////        System.out.println("send here " + Arrays.toString(ewsSimpleSend.getEmail()));
//    }
//}