package com.shpaginAS.recruiter.services;

import com.shpaginAS.recruiter.DTO.VacancyDTO;
import com.shpaginAS.recruiter.models.User;
import com.shpaginAS.recruiter.models.Vacancy;
import com.shpaginAS.recruiter.repository.UserRepository;
import com.shpaginAS.recruiter.repository.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;

@Service
public class VacancyService {

    @Autowired
    private VacancyRepository vacancyRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    public Vacancy createVacancy(VacancyDTO vacancyDTO, Principal principal){

        User recruiter = userService.getCurrentUser(principal);

        Vacancy vacancy = new Vacancy();
        vacancy.setRecruiter(recruiter);
        vacancy.setCompany(vacancyDTO.getCompany());
        vacancy.setProfession(vacancyDTO.getProfession());
        vacancy.setDescription(vacancyDTO.getDescription());
        vacancy.setAdress(vacancyDTO.getAdress());
        vacancy.setCandidateList(new ArrayList<>());
        vacancy.setInjectedCandidateList(new ArrayList<>());
        vacancy.setApprovedCandidateList(new ArrayList<>());

        return vacancyRepository.save(vacancy);
    }
}
