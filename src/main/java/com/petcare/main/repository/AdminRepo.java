package com.petcare.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petcare.main.entities.User;
import java.util.List;


public interface AdminRepo extends JpaRepository<User, Long> {
	
	User findByEmail(String email);
	User findById(long id);

}
