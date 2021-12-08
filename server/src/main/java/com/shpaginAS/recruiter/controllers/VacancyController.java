package com.shpaginAS.recruiter.controllers;

import com.shpaginAS.recruiter.DTO.VacancyDTO;
import com.shpaginAS.recruiter.models.User;
import com.shpaginAS.recruiter.models.Vacancy;
import com.shpaginAS.recruiter.payload.MessageResponse;
import com.shpaginAS.recruiter.repository.VacancyRepository;
import com.shpaginAS.recruiter.services.UserService;
import com.shpaginAS.recruiter.services.VacancyService;
import com.shpaginAS.recruiter.validations.ResponceErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("api/vacancy")
public class VacancyController {

    @Autowired
    private VacancyService vacancyService;
    @Autowired
    private ResponceErrorValidation responceErrorValidation;
    @Autowired
    private UserService userService;
    @Autowired
    VacancyRepository vacancyRepository;

    @PostMapping("/create")
    public ResponseEntity<Object> createVacancy(@Valid @RequestBody VacancyDTO vacancyDTO,
                                             BindingResult bindingResult,
                                             Principal principal) {
        ResponseEntity<Object> errors = responceErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        System.out.println(vacancyDTO.getAdress());
        vacancyService.createVacancy(vacancyDTO, principal);

        return ResponseEntity.ok(new MessageResponse("Vacancy crested!"));
    }

    @PostMapping("/search")
    public ResponseEntity<Object> seacrhVacancy(@Valid @RequestBody String str){

        Iterable<Vacancy> vacancyList = vacancyRepository.findAll();
        ArrayList<Vacancy> result = new ArrayList<>();

        if(str.isEmpty()){
            return new ResponseEntity<>(vacancyList, HttpStatus.OK);
        }

        for(Vacancy vacancy : vacancyList){
            if(vacancy.getProfession().contains(str) || vacancy.getProfession().toUpperCase().contains(str) ||
                vacancy.getProfession().toLowerCase().contains(str)){
                result.add(vacancy);
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/canApplyVacancy/{id}")
    public boolean canApplyVacancy(@PathVariable Long id, Principal principal){

        Optional<Vacancy> op = vacancyRepository.findById(id);
        Vacancy vacancy = op.get();
        User user = userService.getCurrentUser(principal);

        ArrayList<String> list = new ArrayList<>();
        for(User users : vacancy.getCandidateList()){
            list.add(users.getEmail());
        }

        ArrayList<String> injectedList = new ArrayList<>();
        for(User users : vacancy.getInjectedCandidateList()){
            injectedList.add(users.getEmail());
        }

        ArrayList<String> approvedList = new ArrayList<>();
        for(User users : vacancy.getApprovedCandidateList()){
            approvedList.add(users.getEmail());
        }

        if(list.contains(user.getEmail()) || approvedList.contains(user.getEmail()) || injectedList.contains(user.getEmail())) return true;
        else return false;
    }

    @PostMapping("/deleteVacancy")
    public ResponseEntity<Object> deleteVacancy(@RequestBody Long id) {

        Optional<Vacancy> op = vacancyRepository.findById(id);
        Vacancy vacancy = op.get();
        vacancyRepository.delete(vacancy);

        return ResponseEntity.ok(new MessageResponse("Vacancy deleted!"));
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody Vacancy vacancy, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responceErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Optional<Vacancy> op = vacancyRepository.findById(vacancy.getId());

        Vacancy vacancyUpdated = op.get();

        vacancyUpdated.setCompany(vacancy.getCompany());
        vacancyUpdated.setProfession(vacancy.getProfession());
        vacancyUpdated.setDescription(vacancy.getDescription());
        vacancyUpdated.setAdress(vacancy.getAdress());

        vacancyRepository.save(vacancyUpdated);

        return new ResponseEntity<>(vacancyUpdated, HttpStatus.OK);
    }

    @PostMapping("/injectCandidate")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody long[] list, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responceErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Optional<Vacancy> op = vacancyRepository.findById(list[0]);
        Vacancy vacancy = op.get();

        for(int i = 0; i < vacancy.getCandidateList().size(); i++){
            if(vacancy.getCandidateList().get(i).getId() == list[1]){
                vacancy.getInjectedCandidateList().add(vacancy.getCandidateList().get(i));
                vacancy.getCandidateList().remove(vacancy.getCandidateList().get(i));

            }
        }

        vacancyRepository.save(vacancy);
        return new ResponseEntity<>(vacancy, HttpStatus.OK);
    }

    @PostMapping("/approveCandidate")
    public ResponseEntity<Object> approveCandidate(@Valid @RequestBody long[] list, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responceErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Optional<Vacancy> op = vacancyRepository.findById(list[0]);
        Vacancy vacancy = op.get();

        for(int i = 0; i < vacancy.getCandidateList().size(); i++){
            if(vacancy.getCandidateList().get(i).getId() == list[1]){
                vacancy.getApprovedCandidateList().add(vacancy.getCandidateList().get(i));
                vacancy.getCandidateList().remove(vacancy.getCandidateList().get(i));
            }
        }
        vacancyRepository.save(vacancy);
        return new ResponseEntity<>(vacancy, HttpStatus.OK);
    }

    @GetMapping("/vacancys")
    public ResponseEntity<List<Vacancy>> getAllVacancysForUser(Principal principal) {

        List<Vacancy> vacancyList = userService.getAllVacancysForUser(principal);
        return new ResponseEntity<>(vacancyList, HttpStatus.OK);
    }

    @GetMapping("/injectedVacancyForUser")
    public ResponseEntity<List<Vacancy>> getInjectedVacancysForUser(Principal principal) {

        List<Vacancy> vacancyList = userService.getInjectedVacancysForUser(principal);
        return new ResponseEntity<>(vacancyList, HttpStatus.OK);
    }

    @GetMapping("/acceptedVacancyForUser")
    public ResponseEntity<List<Vacancy>> getAcceptedVacancysForUser(Principal principal) {

        List<Vacancy> vacancyList = userService.getAcceptedVacancysForUser(principal);
        return new ResponseEntity<>(vacancyList, HttpStatus.OK);
    }

    @GetMapping("/vacancy/{id}")
    public ResponseEntity<Vacancy> getUserProfile(@PathVariable("id") Long id) {

        Optional<Vacancy> op = vacancyRepository.findById(id);
        Vacancy vacancy = op.get();
        return new ResponseEntity<>(vacancy, HttpStatus.OK);
    }

    @PostMapping("/vacancy/apply")
    public ResponseEntity<MessageResponse> applyForVacancy(@Valid @RequestBody Vacancy vacancy, Principal principal) {

        User user = userService.getCurrentUser(principal);

        if(vacancy.getCandidateList() == null){
            ArrayList<User> list = new ArrayList<>();
            list.add(user);
            vacancy.setCandidateList(list);
            vacancyRepository.save(vacancy);
        } else {
            vacancy.getCandidateList().add(user);
            vacancyRepository.save(vacancy);
        }

        return ResponseEntity.ok(new MessageResponse("Кандидат добавлен!"));
    }

    @GetMapping("/vacancyList")
    public ResponseEntity<List<Vacancy>> getVacancyList(Principal principal) {

        User user = userService.getCurrentUser(principal);
        List<Vacancy> vacancyList = vacancyRepository.findAll();
        ArrayList<Vacancy> result = new ArrayList<>();

        if(user.getRole().equals("Рекрутер")){
            return new ResponseEntity<>(vacancyList, HttpStatus.OK);
        }

        if(user.getRole().equals("Работник")){
            for(Vacancy vacancy : vacancyList){
                if(!vacancy.getCandidateList().contains(user) && !vacancy.getInjectedCandidateList().contains(user) && !vacancy.getApprovedCandidateList().contains(user)){
                    result.add(vacancy);
                }
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(vacancyList, HttpStatus.OK);
    }
}