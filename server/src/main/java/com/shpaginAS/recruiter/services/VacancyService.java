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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VacancyService {

    private final VacancyRepository vacancyRepository;
    private final UserService userService;

    @Autowired
    public VacancyService(VacancyRepository vacancyRepository, UserService userService, UserRepository userRepository){
        this.vacancyRepository = vacancyRepository;
        this.userService = userService;
    }

    public void createVacancy(VacancyDTO vacancyDTO, Principal principal){

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
        vacancyRepository.save(vacancy);
    }

    public List<Vacancy> getVacancyListForUser(Principal principal){
        User user = userService.getCurrentUser(principal);
        List<Vacancy> vacancyList = vacancyRepository.findAll();

        if(user.getRole().equals("Работник")){
             List<Vacancy> list = vacancyList.stream().filter((s) -> !s.getCandidateList().contains(user) && !s.getInjectedCandidateList().contains(user) &&
                    !s.getApprovedCandidateList().contains(user)).collect(Collectors.toList());
             return list;
        }
        else return vacancyList;

    }

    public List<Vacancy> getSearchVacancyForUser(String str, Principal principal){
        List<Vacancy> list = getVacancyListForUser(principal);
        List<Vacancy> searchVacancyListForUser = new ArrayList<>();
        for(Vacancy vacancy : list){
            if(vacancy.getProfession().contains(str) || vacancy.getProfession().toUpperCase().contains(str) ||
                    vacancy.getProfession().toLowerCase().contains(str)){
                searchVacancyListForUser.add(vacancy);
            }
        }
        return searchVacancyListForUser;
    }

    public void updateVacancy(Vacancy vacancy){
        Optional<Vacancy> op = vacancyRepository.findById(vacancy.getId());
        Vacancy vacancyUpdated = op.get();

        vacancyUpdated.setCompany(vacancy.getCompany());
        vacancyUpdated.setProfession(vacancy.getProfession());
        vacancyUpdated.setDescription(vacancy.getDescription());
        vacancyUpdated.setAdress(vacancy.getAdress());

        vacancyRepository.save(vacancyUpdated);
    }
}
