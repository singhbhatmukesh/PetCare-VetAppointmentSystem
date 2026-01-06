package com.petcare.main.resendmailservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.petcare.main.entities.Appointment;
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
			System.out.println("Error sending email");
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
	
	@Async
	public void sendEmailVerification(User user,String token){
		String link=url+"/user/verify?token="+token;
		String htmlForEmailMessage="""
				 <h2>Hi!</h2>
               <p>Click the link below to verify your account:</p>
               <a href="%s" target="_blank"
                  style="padding:10px 15px; background:#4CAF50; color:white; text-decoration:none;">
                  Verify Account
               </a>

				""".formatted(link);
		sendEmail(user.getEmail(), "Verify Account", htmlForEmailMessage);
	}
	
public void sendAppointmentNotification(Appointment appointment, User user){
		
		
		String Subject="Appointment Scheduled";
		String htmlForAppointmentMessage = """
			    <h2>Appointment Confirmed 🐾</h2>
			    <p>Hi <strong>%s</strong>,</p>

			    <p>Your appointment has been successfully booked with 
			       <strong>Dr. %s</strong>.</p>

			    <p><strong>Appointment Details:</strong></p>
			    <ul>
			        <li><strong>Date:</strong> %s</li>
			        <li><strong>Time:</strong> %s</li>
			        <li><strong>Pet:</strong> %s</li>
			    </ul>

			    <p>Please don’t forget to visit on time. We look forward to taking care of your pet 🐶🐱</p>

			    <p style="margin-top:20px;">
			        Thank you for choosing PetCare.</strong>.
			    </p>

			    <p style="font-size:12px;color:gray;">
			        If you did not schedule this appointment, please contact us immediately.
			    </p>
			""".formatted(
			        user.getName(),                 // %s → User name
			        appointment.getVet().getName(), // %s → Vet name
			        //appointment.getVet().getClinicName(), // %s → Vet / clinic
			        appointment.getDate(),          // %s → Date
			        appointment.getTime(),          // %s → Time
			        appointment.getPet().getName(), // %s → Pet name
			        appointment.getVet().getName() // %s → Vet name again
			);
		sendEmail(user.getEmail(), Subject, htmlForAppointmentMessage);
	}

public void sendResetPasswordLinkForAdmin(User user)  {
	Long id = user.getId();
	String link = url+"/admin/resetPassword?id="+id;
	
	
	String to = user.getEmail();
	String Subject ="Reset Password";
		 String htmlForEmailMessage="""
		<h2>Hi!</h2>
            <p>Click the link below to reset you password:</p>
            <a href="%s" target="_blank"
               style="padding:10px 15px; background:#4CAF50; color:white; text-decoration:none;">
               Reset Password
            </a>

			""".formatted(link);
	sendEmail(to, Subject, htmlForEmailMessage);
}

}
