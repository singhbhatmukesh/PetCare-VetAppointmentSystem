package com.petcare.main.service;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import com.petcare.main.entities.Pet;
import com.petcare.main.entities.User;

public interface AdminService {
	
	public List<User> allUsers();
	public Optional<User>  getAdminByEmail(String email);
	public User getAdminById(Long id);
	public User saveAdmin(User admin);
	//petsservicebyadmin
	public Page<Pet> getAllPets(int page);
	public Pet getPetById(Long id);
	public void editPet(Pet pet);
	public void addPet(Pet pet);
	public void deletePet(Long id);
	
	public void resetPassword(Long id, String newPassword, String confirmPassword);

}
