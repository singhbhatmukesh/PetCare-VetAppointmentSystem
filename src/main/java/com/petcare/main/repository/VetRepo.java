package com.petcare.main.repository;

import java.awt.print.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.petcare.main.entities.Vet;
import java.util.List;


public interface VetRepo extends JpaRepository<Vet, Long> {

	Vet findByEmail(String email);
	
	List<Vet> findByIsavailable(boolean available);
	
	
	

}
