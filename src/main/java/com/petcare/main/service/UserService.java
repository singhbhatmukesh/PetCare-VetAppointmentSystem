package com.petcare.main.service;
import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import com.petcare.main.dto.UserDto;
import com.petcare.main.entities.User;

import jakarta.mail.MessagingException;

public interface UserService {
	
	public User saveUser(User user) throws IOException;
	
	public Page<UserDto> getAllUsers(int page);
	public List<User> getAllUsers();
	
	 public User getUserById(long id);
	 public void updateUser(User user, String newPassword);
	 public void deleteUser(long id);
	 public User getUserByEmail(String email);
	 public void changePassword(String email,
			 					String currentPassword,
			 					String newPassword,
			 					String confirmPassword);
	 
	 public void setPassword(String email,
				String newPassword,
				String confirmPassword);
	 
	 

}
