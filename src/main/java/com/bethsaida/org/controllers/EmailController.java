package com.bethsaida.org.controllers;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bethsaida.org.models.User;
import com.bethsaida.org.service.EmailService;

@RestController
public class EmailController  {
	
	@Autowired
	private EmailService emailService;

    @RequestMapping(value = "/sendemail")
	public String sendEmail(User user) throws AddressException, MessagingException, IOException {
	emailService.sendUserRegistrationMail(user);
	return "Email sent successfully";   
	} 
}