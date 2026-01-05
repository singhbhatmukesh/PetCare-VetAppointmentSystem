package com.petcare.main.dtomapper;

import com.petcare.main.dto.AppointmentDto;
import com.petcare.main.entities.Appointment;

public class AppointmentDtoMapper {
	
	public static AppointmentDto toDto(Appointment appointment) {
		AppointmentDto dto = new AppointmentDto();
		
		dto.setDate(appointment.getDate());
		dto.setTime(appointment.getTime());
		dto.setStatus(appointment.getStatus());
		dto.setPetId(appointment.getPet()!=null ? appointment.getPet().getId():null);
		dto.setVetId(appointment.getVet()!=null ? appointment.getVet().getId():null);
		dto.setUserId(appointment.getUser()!=null? appointment.getUser().getId():null);
		return dto;
	}

}
