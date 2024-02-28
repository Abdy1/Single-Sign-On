package com.cbo.sso.services;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EWSAuthenticationService {

    @Autowired
    private ExchangeService exchangeService;

    public String doEWSOperation() throws Exception {
        // Example: Get the root folder
        Folder rootFolder = Folder.bind(exchangeService, WellKnownFolderName.MsgFolderRoot);
        return "EWS operation successful. Root folder name: " + rootFolder.getDisplayName();
    }
    public void sendEmail() throws Exception {
        EmailMessage message = new EmailMessage(exchangeService);
        message.setSubject("Test Subject");
        message.setBody(MessageBody.getMessageBodyFromText("Test Body"));
        message.getToRecipients().add("Abdellah.Kamil@coopbankoromiasc.com");
        message.send();
    }
}