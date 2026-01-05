package com.petcare.main.dtomapper;

import com.petcare.main.dto.UserDto;
import com.petcare.main.entities.User;

public class UserDtoMapper {
	
	public static UserDto toDto(User user) {
		UserDto udto = new UserDto();
		udto.setId(user.getId());
		udto.setName(user.getName());
		udto.setEmail(user.getEmail());
		udto.setGender(user.getGender());
		udto.setRole(user.getRole());
		return udto;
	}
}
