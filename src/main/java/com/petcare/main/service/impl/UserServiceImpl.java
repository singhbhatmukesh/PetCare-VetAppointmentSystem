package com.petcare.main.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.petcare.main.dto.UserDto;
import com.petcare.main.entities.User;
import com.petcare.main.entities.Verificationtoken;
import com.petcare.main.repository.UserRepo;
import com.petcare.main.service.UserService;
import com.petcare.main.utilities.CreateVerificationToken;
import com.petcare.main.utilities.EmailService;

import jakarta.mail.MessagingException;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private UserRepo uRepo;
	
	@Autowired
	private CreateVerificationToken cvt;
	
	@Autowired
	private EmailService eservice;

	@Override
		public User saveUser(User user) throws MessagingException {
			if(uRepo.findByEmail(user.getEmail()).isPresent()) {
				throw new IllegalStateException("Existing_email");
			}
			
			if(user.getRole()==null) {
				user.setRole("ROLE_USER");
			}
			if(user.getProvider()==null) {
				user.setProvider("LOCAL");
			}
			user.setEnabled(false);
			String password = encoder.encode(user.getPassword());
			user.setPassword(password);
			
			Verificationtoken token = cvt.createToken();
			token.setUser(user);
			user.setToken(token);
			User savedUser = uRepo.save(user);
			
			
			
			
			eservice.sendEmailVerification(savedUser, token.getToken());
			
			return user;
		}


	@Override
	public Page<UserDto> getAllUsers(int page) {
		
		Pageable pageable = PageRequest.of(page, 5);
		Page<User> usersPage = uRepo.findAll(pageable);
		List<UserDto> userDtos = new ArrayList<>();
		for(User user : usersPage.getContent()) {
			UserDto udto = new UserDto();
			udto.setId(user.getId());
			udto.setEmail(user.getEmail());
			udto.setGender(user.getGender());
			udto.setName(user.getName());
			udto.setRole(user.getRole());
			udto.setProvider(user.getProvider());
			
			userDtos.add(udto);
		}
		
		return new PageImpl<>(userDtos, pageable, usersPage.getTotalElements());
	}

	@Override
	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return uRepo.findAll();
	}



	@Override
	public User getUserById(long id) {
		User userById = uRepo.findById(id);
		
		
		return userById;
	}



	@Override
	public void deleteUser(long id) {
		User byId = uRepo.findById(id);
		uRepo.delete(byId);
	}



	@Override
	public void updateUser(User user, String newPassword) {
		User byid = uRepo.findById(user.getId());
		byid.setEmail(user.getEmail());
		byid.setGender(user.getGender());
		byid.setName(user.getName());
		byid.setRole(user.getRole());
		
		if(newPassword!=null && !newPassword.isBlank()) {
			byid.setPassword(encoder.encode(newPassword));
		}
		uRepo.save(byid);
	}

	@Override
	public User getUserByEmail(String email) {
		User userByEmail = uRepo.findByEmail(email).orElseThrow(()->
												 new RuntimeException("User doesnot exist"));
		userByEmail.getPet().size();
		return userByEmail;
	}



	@Override
	public void changePassword(String email, 
							   String currentPassword, 
							   String newPassword, 
							   String confirmPassword) {
		User userByEmail = getUserByEmail(email);
		if(!encoder.matches(currentPassword, userByEmail.getPassword())) {
			throw new IllegalArgumentException("Current Password doesnot match.");
		}
		if(!newPassword.equals(confirmPassword)) {
			throw new IllegalArgumentException("New and Confirm password must be same.");
		}
		if(encoder.matches(newPassword, userByEmail.getPassword())) {
			throw new IllegalArgumentException("New password must be different from old one.");
			}
		userByEmail.setPassword(encoder.encode(newPassword));
		uRepo.save(userByEmail);
		
	}


	@Override
	public void setPassword(String email, String newPassword, String confirmPassword) {
		User existinguser = getUserByEmail(email);
		
		if(!newPassword.equals(confirmPassword)) {
			throw new IllegalArgumentException("New and Confirm password must be same.");
		}
		
		existinguser.setPassword(encoder.encode(newPassword));
		uRepo.save(existinguser);
		
	}
	
	
	
    

}
