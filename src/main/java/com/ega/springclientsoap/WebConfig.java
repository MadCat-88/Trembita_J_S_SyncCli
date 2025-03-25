/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ega.springclientsoap;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPMessage;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

/**
 *
 * @author sa
 */
@Configuration
public class WebConfig {
    public WebServiceTemplate web;
    public SOAPMessage message;
    
    public WebConfig(){
        this.web = InitWebService();
    }

    private WebServiceTemplate InitWebService(){
    try {
            SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory(MessageFactory.newInstance());
            this.message = messageFactory.getMessageFactory().createMessage();
            //SOAPHeader header = this.message.getSOAPHeader();
            //SOAPElement element = header.addChildElement("name", "string");
            //this.message.saveChanges();
            messageFactory.afterPropertiesSet();
            WebServiceTemplate webServiceTemplate = new WebServiceTemplate(messageFactory);
            Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
            marshaller.setContextPath("io.spring.guides.gs_producing_web_service");
            marshaller.afterPropertiesSet();

            webServiceTemplate.setMarshaller(marshaller);
            webServiceTemplate.setUnmarshaller(marshaller);
            webServiceTemplate.afterPropertiesSet();
        
            return webServiceTemplate;
        }
        catch(Exception ex){
            return null;
        }
        
    }
    
    public void SetHeader(String key, String value){
        
        WebServiceMessageFactory messageFactory = this.web.getMessageFactory();
        SOAPHeader header;
        try {
            header = this.message.getSOAPHeader();
            SOAPElement element = header.addChildElement(key, value);
            this.message.saveChanges();
        } catch (SOAPException ex) {
        }
        
    }
}
