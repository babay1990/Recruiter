package com.shpaginAS.recruiter.facade;

import com.shpaginAS.recruiter.DTO.UserDTO;
import com.shpaginAS.recruiter.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {
    public UserDTO userToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setLastname(user.getLastname());
        userDTO.setProfession(user.getProfession());
        userDTO.setRole(user.getRole());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setImageBytes(user.getImageBytes());
        userDTO.setSummaryBytes(user.getSummaryBytes());
        userDTO.setEmployeeVacancyList(user.getEmployeeVacancyList());
        userDTO.setRecruiterVacancyList(user.getRecruiterVacancyList());
        return userDTO;
    }
}