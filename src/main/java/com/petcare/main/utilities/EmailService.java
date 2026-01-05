package com.petcare.main.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.petcare.main.entities.Appointment;
import com.petcare.main.entities.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender sender;
	
	@Value("${emailVerification.url}")
	String url;
	
	public void sendEmailVerification(User user,String token) throws MessagingException {
		String link=url+"/user/verify?token="+token;
		
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		
		helper.setTo(user.getEmail());
		helper.setSubject("User Email  Verification");
		String htmlForEmailMessage="""
				 <h2>Hi!</h2>
                <p>Click the link below to verify your account:</p>
                <a href="%s" target="_blank"
                   style="padding:10px 15px; background:#4CAF50; color:white; text-decoration:none;">
                   Verify Account
                </a>

				""".formatted(link);
		
		helper.setText(htmlForEmailMessage,true);
		sender.send(message);
	}
	
	public void sendResetPasswordLink(User user) throws MessagingException {
		Long id = user.getId();
		String link = url+"/user/resetPassword?id="+id;
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		
		helper.setTo(user.getEmail());
		helper.setSubject("Reset Password");
			 String htmlForEmailMessage="""
			<h2>Hi!</h2>
                <p>Click the link below to reset you password:</p>
                <a href="%s" target="_blank"
                   style="padding:10px 15px; background:#4CAF50; color:white; text-decoration:none;">
                   Reset Password
                </a>

				""".formatted(link);
		
		helper.setText(htmlForEmailMessage,true);
		sender.send(message);
	}
	
	public void sendAppointmentNotification(Appointment appointment, User user) throws MessagingException {
		
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		
		helper.setTo(user.getEmail());
		helper.setSubject("Appointment Scheduled");
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

		
		helper.setText(htmlForAppointmentMessage,true);
		sender.send(message);
	}
	
	public void sendResetPasswordLinkForAdmin(User user) throws MessagingException {
		Long id = user.getId();
		String link = url+"/admin/resetPassword?id="+id;
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		
		helper.setTo(user.getEmail());
		helper.setSubject("Reset Password");
			 String htmlForEmailMessage="""
			<h2>Hi!</h2>
                <p>Click the link below to reset you password:</p>
                <a href="%s" target="_blank"
                   style="padding:10px 15px; background:#4CAF50; color:white; text-decoration:none;">
                   Reset Password
                </a>

				""".formatted(link);
		
		helper.setText(htmlForEmailMessage,true);
		sender.send(message);
	}


}
