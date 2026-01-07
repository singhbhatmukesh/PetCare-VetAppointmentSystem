package com.petcare.main.sendGrid;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.petcare.main.entities.Appointment;
import com.petcare.main.entities.User;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class SendGridEmailService {
	
	 @Value("${emailVerification.url}")
	    private String url;
	
	private final SendGrid sendGrid;

	public SendGridEmailService(SendGrid sendGrid) {
		super();
		this.sendGrid = sendGrid;
	}
	
	
	
	public void sendEmail(String to,String subject,String html) throws IOException {
		Email toEmail = new Email(to);
		Email fromEmail=new Email("bhatm836@gmail.com", "PetCare");
		Content content = new Content("text/html", html);
		
		Mail mail = new Mail(fromEmail, subject, toEmail, content);
		
		Request req = new Request();
		
		req.setMethod(Method.POST);
		req.setEndpoint("mail/send");
		req.setBody(mail.build());
		
		sendGrid.api(req);
	}
	
	  // ---------- RESET PASSWORD (USER) ----------
    @Async
    public void sendResetPasswordLink(User user) throws IOException {

        String link = url + "/user/resetPassword?id=" + user.getId();

        String html = """
                <h2>Hi!</h2>
                <p>Click the link below to reset your password:</p>
                <a href="%s" target="_blank"
                   style="padding:10px 15px; background:#4CAF50; color:white; text-decoration:none;">
                   Reset Password
                </a>
                """.formatted(link);

        sendEmail(user.getEmail(), "Reset Password", html);
    }
    
    @Async
    public void sendEmailVerification(User user, String token) throws IOException {

        String link = url + "/user/verify?token=" + token;

        String html = """
                <h2>Hi!</h2>
                <p>Click the link below to verify your account:</p>
                <a href="%s" target="_blank"
                   style="padding:10px 15px; background:#4CAF50; color:white; text-decoration:none;">
                   Verify Account
                </a>
                """.formatted(link);

        sendEmail(user.getEmail(), "Verify Account", html);
    }

    // ---------- APPOINTMENT NOTIFICATION ----------
    @Async
    public void sendAppointmentNotification(Appointment appointment, User user) throws IOException {

        String html = """
                <h2>Appointment Confirmed 🐾</h2>
                <p>Hi <strong>%s</strong>,</p>

                <p>Your appointment has been successfully booked with
                   <strong>%s</strong>.</p>

                <p><strong>Appointment Details:</strong></p>
                <ul>
                    <li><strong>Date:</strong> %s</li>
                    <li><strong>Time:</strong> %s</li>
                    <li><strong>Pet:</strong> %s</li>
                </ul>

                <p>Please don’t forget to visit on time.
                We look forward to taking care of your pet 🐶🐱</p>

                <p style="margin-top:20px;">
                    Thank you for choosing <strong>PetCare</strong>.
                </p>

                <p style="font-size:12px;color:gray;">
                    If you did not schedule this appointment, please contact us immediately.
                </p>
                """.formatted(
                        user.getName(),
                        appointment.getVet().getName(),
                        appointment.getDate(),
                        appointment.getTime(),
                        appointment.getPet().getName()
                );

        sendEmail(user.getEmail(), "Appointment Scheduled", html);
    }

    // ---------- RESET PASSWORD (ADMIN) ----------
    @Async
    public void sendResetPasswordLinkForAdmin(User user) throws IOException {

        String link = url + "/admin/resetPassword?id=" + user.getId();

        String html = """
                <h2>Hi!</h2>
                <p>Click the link below to reset your password:</p>
                <a href="%s" target="_blank"
                   style="padding:10px 15px; background:#4CAF50; color:white; text-decoration:none;">
                   Reset Password
                </a>
                """.formatted(link);

        sendEmail(user.getEmail(), "Reset Password", html);
    }

}
