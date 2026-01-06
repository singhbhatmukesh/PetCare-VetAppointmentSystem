package com.petcare.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.petcare.main.customExceptions.TokenExpiredException;
import com.petcare.main.entities.User;
import com.petcare.main.repository.UserRepo;
import com.petcare.main.service.UserService;
import com.petcare.main.utilities.EmailVerificationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class EmailVerificationController {
	
	@Autowired
	private UserRepo urepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailVerificationService eservice;
	
	@Autowired
	private UserService uservice;
	
	@GetMapping("/verification-result")
	public String verificationResult() {
	    return "user/EmailVerification/after-verification-page";
	}

	
	@GetMapping("/verify")
	public String verifyEmail(@RequestParam("token") String token, RedirectAttributes ra) {
		try {
			eservice.verifyUser(token);
			ra.addFlashAttribute("success", "Verification Successful");
			
		} catch (TokenExpiredException e) {
			ra.addFlashAttribute("error", e.getMessage());
		}
		catch (IllegalArgumentException e) {
			ra.addFlashAttribute("error", e.getMessage());
		}
		
	 return "redirect:/user/verification-result";
	}
	@GetMapping("/resetPassword")
	public String resetPassword(@RequestParam Long id, Model model) {
		User userById = uservice.getUserById(id);
		model.addAttribute("user", userById);
		return "user/resetpassword-after-emailverification-page";
	}
	
	@PostMapping("/reset-password")
	public String resetPassword(@RequestParam Long userId,
								@RequestParam String newPassword,
								@RequestParam String confirmPassword,
								RedirectAttributes ra,
								HttpServletRequest request) {
		
		User existinguser = uservice.getUserById(userId);
		if(passwordEncoder.matches(newPassword, existinguser.getPassword())) {
		ra.addFlashAttribute("error", "New password must be different from existing one.");
		return "redirect:/user/resetPassword?id="+userId;
		}
		if(!newPassword.equals(confirmPassword)) {
			ra.addFlashAttribute("error", "Confirm password and new password didnot match.");
			return "redirect:/user/resetPassword?id="+userId;
		}
		String newpassword = passwordEncoder.encode(newPassword);
		existinguser.setPassword(newpassword);
		urepo.save(existinguser);
		HttpSession session = request.getSession(false);
		if (session != null) {
		    session.invalidate();
		}

		ra.addFlashAttribute("success", "Password changed!!!");
		return "redirect:/user/loginPage";
	}

}
