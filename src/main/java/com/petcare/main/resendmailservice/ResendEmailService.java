package com.petcare.main.resendmailservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.petcare.main.entities.User;
import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class ResendEmailService {
	
	private Resend resend;

	public ResendEmailService(Resend resend) {
		super();
		this.resend = resend;
	}
	
	@Value("${emailVerification.url}")
	String url;
	
	private static final String From ="PetCare <onboarding@resend.dev>";
	
	//------CORE MAIL LOGIC----------
	
	public void sendEmail(String to, String subject, String html) {
		CreateEmailOptions createEmailOptions = CreateEmailOptions.builder()
												.from(From)
												.to(to)
												.subject(subject)
												.html(html)
												.build();
		try {
			resend.emails().send(createEmailOptions);
			
		} catch (Exception e) {
			System.out.println("Error sending email failed");
		}
		
	}
	
	@Async
	public void sendResetPasswordLink(User user)  {
		Long id = user.getId();
		String link = url+"/user/resetPassword?id="+id;
		 String htmlForEmailMessage="""
					<h2>Hi!</h2>
		                <p>Click the link below to reset you password:</p>
		                <a href="%s" target="_blank"
		                   style="padding:10px 15px; background:#4CAF50; color:white; text-decoration:none;">
		                   Reset Password
		                </a>

						""".formatted(link);
		 String to = user.getEmail();
		 String Subject="Reset Password";
		 sendEmail(to, Subject, htmlForEmailMessage);
		
		
		
	}

}
