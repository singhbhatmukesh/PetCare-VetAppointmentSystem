package com.petcare.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.petcare.main.entities.Verificationtoken;


public interface VerificationTokenRepo extends JpaRepository<Verificationtoken, Long> {

Verificationtoken findByToken(String token);
}
