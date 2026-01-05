package com.petcare.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.petcare.main.entities.Appointment;
import com.petcare.main.entities.Pet;
import com.petcare.main.entities.User;
import com.petcare.main.entities.Vet;

import java.util.List;


public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
	
	//for scheduled
	List<Appointment> findByStatus(String status);
	
	//for history
	List<Appointment> findByStatusIn(List<String> status);
	
	//for clearing history
	@Modifying
	@Query("""
			DELETE FROM Appointment a
			WHERE a.status IN('Completed','Canceled')	
			""")
	void deleteHistory();

	//for User
	List<Appointment> findByUserAndStatus(User user, String status);
	
	//for Vet
	List<Appointment> findByVetAndStatus(Vet vet, String status);
	
	@Query("""
			SELECT DISTINCT a.pet
			FROM Appointment a
			WHERE a.vet = :vet
			AND a.status = 'Scheduled'
			""")
	List<Pet> getAssignedPets(@Param("vet") Vet vet);
	
	

}
