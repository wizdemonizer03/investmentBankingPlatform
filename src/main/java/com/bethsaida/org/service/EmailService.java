package com.bethsaida.org.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bethsaida.org.models.Customer;
import com.bethsaida.org.models.Investment;
import com.bethsaida.org.models.User;
import com.bethsaida.org.repository.UserAccountRepository;
import com.sun.net.httpserver.Authenticator;

@Service
public class EmailService {
	
	@Autowired 
	private UserAccountRepository userRepository;
	
   
   public void sendUserRegistrationMail(User user) throws AddressException, MessagingException, IOException {
	   Properties props = new Properties();
	   props.put("mail.smtp.auth", "true");
	   props.put("mail.smtp.starttls.enable", "true");
	   props.put("mail.smtp.host", "smtp.gmail.com");
	   props.put("mail.smtp.port", "587");
	   
	    String htmlMessage = "<body style=\"width: 570px; height: fit-content;\">\r\n" + 
	   		"<div>\r\n" + 
	   		"<h2 style=\"text-align:center; color: #000000; line-height:32px\"> Welcome To Trovest Investment Platform </h2>\r\n" + 
	   		"</div>\r\n" + 
	   		"<div style=\"font-family:Open Sans, Helvetica, Arial, sans-serif;font-size:13px;line-height:22px;text-align:left;color:#797e82;\">\r\n" + 
	   		"<p style=\"margin: 10px 0; text-align: center;\">Hello   "  + user.getFirstName()  +  "  You are Welcome to Trovest Investment platform</p>\r\n" + 
	   		"<p style=\"margin: 10px 0; text-align: center;\">To gain access to your account, here is your Login Information</p>\r\n" + 
	   		"</div>\r\n" + 
	   		"\r\n" + 
	   		"<div style=\"font-family:Open Sans, Helvetica, Arial, sans-serif;font-size:13px;line-height:22px;text-align:left;color:#797e82;\">\r\n" + 
	   		"<p style=\"margin: 10px 0; text-align: center;\"><b>Username : </b>"  + user.getUserName() + "</p>\r\n" + 
	   		"<p style=\"margin: 10px 0; text-align: center;\"><b>Password : </b>" + user.getPassword() + "</p>\r\n" + 
	   		"</div>\r\n" + 
	   		
            "<div style=\"font-family:Open Sans, Helvetica, Arial, sans-serif;font-size:13px;line-height:22px;text-align:left;color:#797e82;\"> "
            + "<p style=\"margin: 10px 0; text-align: center; \">To Login to your account area </p>" +
            "</div>\r\n" +
	   		
	   	    "<div style=\"margin: 10px 0; text-align: center; \"> "+
	   	    "<p><a style=\"margin: 10px 0; background-color:dark-blue; font-color: white;\" href=http://demo.techytal.com/ >Click Here</a></p>" +
	   	    "</div>\r\n" +

	   		"\r\n" + 
	   		"</body>";
	   
	   Session session = Session.getInstance(props, new javax.mail.Authenticator() {
	   protected PasswordAuthentication getPasswordAuthentication() {
	   return new PasswordAuthentication("africabethsaida@gmail.com", "ikhichoioiuvcgjm");
	    }
	   });
	   Message msg = new MimeMessage(session);
	   msg.setFrom(new InternetAddress("Trovest Investment Platform <africabethsaida@gmail.com>", true));

	   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
	   msg.setSubject("Login Details - Trovest Investment Platform");
	   //msg.setContent("Dear " + user.getFirstName() + "Welcome to Bethsaida Investment platform .This is your Username:" + user.getUserName() + "this is your password :" +user.getPassword(), "text/html");
	   msg.setContent(htmlMessage, "text/html");
	   msg.setSentDate(new Date());

	   MimeBodyPart messageBodyPart = new MimeBodyPart();
	   messageBodyPart.setContent("Working P", "text/html");
       Transport.send(msg);   
	}

   
   
       public void sendCustomerRegistrationMail(Customer customer) throws AddressException, MessagingException, IOException {
	
    	//   String emailTrail = "tech-support@bethsaidaafrica.com";
    	    
    	   Properties props = new Properties();
    	   props.put("mail.smtp.auth", "true");
    	   props.put("mail.smtp.starttls.enable", "true");
    	   props.put("mail.smtp.host", "smtp.gmail.com");
    	   props.put("mail.smtp.port", "587");
       
    	   String htmlMessage = "<body style=\"width: 570px; height: fit-content;\">\r\n" + 
    		   		"<div>\r\n" + 
    		   		"<h2 style=\"text-align:center; color: #000000; line-height:32px\"> Welcome To Trovest Investment Platform </h2>\r\n" + 
    		   		"</div>\r\n" + 
    		   		"<div style=\"font-family:Open Sans, Helvetica, Arial, sans-serif;font-size:13px;line-height:22px;text-align:left;color:#797e82;\">\r\n" + 
    		   		"<p style=\"margin: 10px 0; text-align: center;\">Hello   "  + customer.getFirstName()  +  "  You are Welcome to Trovest Investment Platform</p>\r\n" + 
    		   		"<p style=\"margin: 10px 0; text-align: center;\">To gain access to your account, here is your Login Information</p>\r\n" + 
    		   		"</div>\r\n" + 
    		   		"\r\n" + 
    		   		"<div style=\"font-family:Open Sans, Helvetica, Arial, sans-serif;font-size:13px;line-height:22px;text-align:left;color:#797e82;\">\r\n" + 
    		   		"<p style=\"margin: 10px 0; text-align: center;\"><b>Username : </b>"  + customer.getUserName() + "</p>\r\n" + 
    		   		"<p style=\"margin: 10px 0; text-align: center;\"><b>Password : </b>" + customer.getPassword() + "</p>\r\n" + 
    		   		"</div>\r\n" + 
    		   		
    	            "<div style=\"font-family:Open Sans, Helvetica, Arial, sans-serif;font-size:13px;line-height:22px;text-align:left;color:#797e82;\"> "
    	            + "<p style=\"margin: 10px 0; text-align: center; \">To Login to your account area </p>" +
    	            "</div>\r\n" +
    		   		
    		   	    "<div style=\"margin: 10px 0; text-align: center; \"> "+
    		   	    "<p><a style=\"margin: 10px 0; background-color:dark-blue; font-color: white;\" href=http://ibank.techytal.com/ >Click Here</a></p>" +
    		   	    "</div>\r\n" +

    		   		"\r\n" + 
    		   		"</body>";
    		   
    		   Session session = Session.getInstance(props, new javax.mail.Authenticator() {
    		   protected PasswordAuthentication getPasswordAuthentication() {
    		   return new PasswordAuthentication("africabethsaida@gmail.com", "ikhichoioiuvcgjm");
    		    }
    		   });
    		   Message msg = new MimeMessage(session);
    		   msg.setFrom(new InternetAddress("Trovest Finance <africabethsaida@gmail.com>", true));

    		   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(customer.getEmail()));
    		   msg.setSubject("Customer Login Details - Trovest Investment");
    		   //msg.setContent("Dear " + user.getFirstName() + "Welcome to Bethsaida Investment platform .This is your Username:" + user.getUserName() + "this is your password :" +user.getPassword(), "text/html");
    		   msg.setContent(htmlMessage, "text/html");
    		   msg.setSentDate(new Date());

    		   MimeBodyPart messageBodyPart = new MimeBodyPart();
    		   messageBodyPart.setContent("Working P", "text/html");
    	       Transport.send(msg);   
    		}

       
       
       public void sendLiquidationReviewNotification() throws AddressException, MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.host", "smtp.gmail.com");
    	props.put("mail.smtp.port", "587");
        
    	   String htmlMessage = "<body style=\"width: 570px; height: fit-content;\" >\r\n" + 
   		   		
   		   		"<div style=\"font-family:Open Sans, Helvetica, Arial, sans-serif;font-size:13px;line-height:22px;text-align:left;color:#797e82;\">\r\n" + 
   		   		"<p style=\"margin: 10px 0; text-align: center;\"> Dear Auditor, you have an Investment Liquidation that needs your urgent attention ! </p>\r\n" + 
   		   		"<p style=\"margin: 10px 0; text-align: center;\">Log in to your account on the Investment Platform to perform action as designated to your role." + 
   		   		"</p></div>\r\n" + 
   		   		"\r\n" + 
   		   		
   		   		"</body>";  
    	   
    	   Session session = Session.getInstance(props, new javax.mail.Authenticator() {
    		   protected PasswordAuthentication getPasswordAuthentication() {
    		   return new PasswordAuthentication("africabethsaida@gmail.com", "ikhichoioiuvcgjm");
    		    }
    		   });
    		   Message msg = new MimeMessage(session);
    		   msg.setFrom(new InternetAddress("Trovest Investment<africabethsaida@gmail.com>", true));
    		   
    		   String auditorEmail = userRepository.findAuditorEmail();

    		   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(auditorEmail));
    		   msg.setSubject("Investment Liquidation Notification - Trovest Investment");
    		   
    		   msg.setContent(htmlMessage, "text/html");
    		   msg.setSentDate(new Date());

    		   MimeBodyPart messageBodyPart = new MimeBodyPart();
    		   messageBodyPart.setContent("Working P", "text/html");
    	       Transport.send(msg);   
    		}
    	   
       
       

  public void sendMaturityNotification() throws AddressException, MessagingException, IOException {
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.starttls.enable", "true");
	props.put("mail.smtp.host", "smtp.gmail.com");
	props.put("mail.smtp.port", "587");
    
	   String htmlMessage = "<body style=\"width: 570px; height: fit-content;\">\r\n" + 
		   		
		   		"<div style=\"font-family:Open Sans, Helvetica, Arial, sans-serif;font-size:13px;line-height:22px;text-align:left;color:#797e82;\">\r\n" + 
		   		"<p style=\"margin: 10px 0; text-align: center;\"> Dear Investment Executive, you have an Investment that has Matured and needs your urgent attention ! </p>\r\n" + 
		   		"<p style=\"margin: 10px 0; text-align: center;\">Log in to your account on the Investment Platform to perform action as designated to your role." + 
		   		"</p></div>\r\n" + 
		   		"\r\n" + 
		   		
		   		"</body>";  
	   
	   Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		   protected PasswordAuthentication getPasswordAuthentication() {
		   return new PasswordAuthentication("africabethsaida@gmail.com", "ikhichoioiuvcgjm");
		    }
		   });
		   Message msg = new MimeMessage(session);
		   msg.setFrom(new InternetAddress("Trovest Finance <africabethsaida@gmail.com>", true));
		   
		   String investmentExecEmail = userRepository.findInvestmentExecutiveEmail();

		   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(investmentExecEmail));
		   msg.setSubject("Investment Liquidation Notification - Trovest Investment");
		   
		   msg.setContent(htmlMessage, "text/html");
		   msg.setSentDate(new Date());

		   MimeBodyPart messageBodyPart = new MimeBodyPart();
		   messageBodyPart.setContent("Working P", "text/html");
	       Transport.send(msg);   
		}
  
  
  
  public void sendFirstLineApprovalEmail() throws AddressException, MessagingException, IOException {
	   Properties props = new Properties();
	   props.put("mail.smtp.auth", "true");
	   props.put("mail.smtp.starttls.enable", "true");
	   props.put("mail.smtp.host", "smtp.gmail.com");
	   props.put("mail.smtp.port", "587");
	   
	   String htmlMessage = "<body style=\"width: 570px; height: fit-content;\">\r\n" + 
		   		
		   		"<div style=\"font-family:Open Sans, Helvetica, Arial, sans-serif;font-size:13px;line-height:22px;text-align:left;color:#797e82;\">\r\n" + 
		   		"<p style=\"margin: 10px 0; text-align: center;\"> Dear Managing Director, you have a new Investment profile that needs your attention for approval ! </p>\r\n" + 
		   		"<p style=\"margin: 10px 0; text-align: center;\">Log in to your account on the Investment Platform to perform action as designated to your role." + 
		   		"</p></div>\r\n" + 
		   		"\r\n" + 
		   		
		   		"</body>";  
	   
	   Session session = Session.getInstance(props, new javax.mail.Authenticator() {
	   protected PasswordAuthentication getPasswordAuthentication() {
	   return new PasswordAuthentication("africabethsaida@gmail.com", "ikhichoioiuvcgjm");
	    }
	   });
	   Message msg = new MimeMessage(session);
	   msg.setFrom(new InternetAddress("Trovest Investment <africabethsaida@gmail.com>", true));
       List<String> managingDirectorList = userRepository.getManagingDirectorEmail();
	   for(String directorEmail : managingDirectorList) {
		  msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(directorEmail));
	   }
	   msg.setSubject("Pending Investment Approval - Trovest Investment");
	   msg.setContent(htmlMessage, "text/html");
	   msg.setSentDate(new Date());

	   MimeBodyPart messageBodyPart = new MimeBodyPart();
	   messageBodyPart.setContent("Working P", "text/html");
      Transport.send(msg);   
	}



     public void sendSecondLineApprovalEmail() throws AddressException, MessagingException, IOException {
  	   Properties props = new Properties();
  	   props.put("mail.smtp.auth", "true");
  	   props.put("mail.smtp.starttls.enable", "true");
  	   props.put("mail.smtp.host", "smtp.gmail.com");
  	   props.put("mail.smtp.port", "587");
  	   
  	   String htmlMessage = "<body style=\"width: 570px; height: fit-content;\">\r\n" + 
  		   		
  		   		"<div style=\"font-family:Open Sans, Helvetica, Arial, sans-serif;font-size:13px;line-height:22px;text-align:left;color:#797e82;\">\r\n" + 
  		   		"<p style=\"margin: 10px 0; text-align: center;\"> Dear Investment Executive, you have a pending Investment profile that needs your attention for final approval ! </p>\r\n" + 
  		   		"<p style=\"margin: 10px 0; text-align: center;\">Log in to your account on the Investment Platform to perform action as designated to your role." + 
  		   		"</p></div>\r\n" + 
  		   		"\r\n" + 
  		   		
  		   		"</body>";  
  	   
  	   Session session = Session.getInstance(props, new javax.mail.Authenticator() {
  	   protected PasswordAuthentication getPasswordAuthentication() {
  	   return new PasswordAuthentication("africabethsaida@gmail.com", "ikhichoioiuvcgjm");
  	    }
  	   });
  	   Message msg = new MimeMessage(session);
  	   msg.setFrom(new InternetAddress("Bethsaida Investment <africabethsaida@gmail.com>", true));
       String investmentExecEmail = userRepository.findInvestmentExecutiveEmail();
  	   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(investmentExecEmail));
  	   msg.setSubject("Pending Investment Approval - Trovest Investment ");
  	   msg.setContent(htmlMessage, "text/html");
  	   msg.setSentDate(new Date());

  	   MimeBodyPart messageBodyPart = new MimeBodyPart();
  	   messageBodyPart.setContent("Working P", "text/html");
        Transport.send(msg);   
  	}

  
  
	   
   }
   
       
       

   
   
   
   
   
