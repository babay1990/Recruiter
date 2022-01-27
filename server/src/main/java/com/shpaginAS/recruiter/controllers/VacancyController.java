package com.shpaginAS.recruiter.controllers;

import com.shpaginAS.recruiter.DTO.VacancyDTO;
import com.shpaginAS.recruiter.models.Message;
import com.shpaginAS.recruiter.models.User;
import com.shpaginAS.recruiter.models.Vacancy;
import com.shpaginAS.recruiter.payload.MessageResponse;
import com.shpaginAS.recruiter.repository.VacancyRepository;
import com.shpaginAS.recruiter.services.KafkaProducerService;
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

    private final VacancyService vacancyService;
    private final ResponceErrorValidation responceErrorValidation;
    private final UserService userService;
    private final VacancyRepository vacancyRepository;
    private final KafkaProducerService producerService;

    @Autowired
    public VacancyController(VacancyService vacancyService, ResponceErrorValidation responceErrorValidation, UserService userService,
                             VacancyRepository vacancyRepository, KafkaProducerService producerService){
        this.vacancyService = vacancyService;
        this.responceErrorValidation = responceErrorValidation;
        this.userService = userService;
        this.vacancyRepository = vacancyRepository;
        this.producerService = producerService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createVacancy(@Valid @RequestBody VacancyDTO vacancyDTO,
                                             BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responceErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        vacancyService.createVacancy(vacancyDTO, principal);
        return ResponseEntity.ok(new MessageResponse("Vacancy created!"));
    }

    @PostMapping("/search")
    public ResponseEntity<Object> findAVacancy(@Valid @RequestBody String str, Principal principal){

        List<Vacancy> searchVacancyListForUser = vacancyService.getSearchVacancyForUser(str, principal);
        return new ResponseEntity<>(searchVacancyListForUser, HttpStatus.OK);
    }

    @GetMapping("/canApplyVacancy/{id}")
    public boolean canApplyVacancy(@PathVariable Long id, Principal principal){

        Optional<Vacancy> op = vacancyRepository.findById(id);
        Vacancy vacancy = op.get();
        User user = userService.getCurrentUser(principal);
        return vacancy.getCandidateList().contains(user) || vacancy.getApprovedCandidateList().contains(user) || vacancy.getInjectedCandidateList().contains(user);
    }

    @PostMapping("/deleteVacancy")
    public ResponseEntity<Object> deleteVacancy(@RequestBody Long id) {

        Optional<Vacancy> op = vacancyRepository.findById(id);
        Vacancy vacancy = op.get();
        vacancyRepository.delete(vacancy);

        return ResponseEntity.ok(new MessageResponse("Vacancy deleted!"));
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateVacancy(@Valid @RequestBody Vacancy vacancy, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responceErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        vacancyService.updateVacancy(vacancy);

        return ResponseEntity.ok(new MessageResponse("Vacancy updated!"));
    }

    //в массиве list передаем id вакансии ([0]) и id кандидата ([1])
    @PostMapping("/injectCandidate")
    public ResponseEntity<Object> rejectCandidate(@Valid @RequestBody long[] list, BindingResult bindingResult) {
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

    //в массиве передаем id вакансии ([0]) и id кандидата ([1])
    @PostMapping("/approveCandidate")
    public ResponseEntity<Object> approveCandidate(@Valid @RequestBody long[] list, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responceErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        Optional<Vacancy> op = vacancyRepository.findById(list[0]);
        Vacancy vacancy = op.get();

        for(int i = 0; i < vacancy.getCandidateList().size(); i++){
            if(vacancy.getCandidateList().get(i).getId() == list[1]){
                vacancy.getApprovedCandidateList().add(vacancy.getCandidateList().get(i));

                producerService.produce(new Message(vacancy.getCandidateList().get(i).getEmail(), vacancy.getCandidateList().get(i).getName(),
                        vacancy.getRecruiter().getName(), vacancy.getRecruiter().getLastname(), vacancy.getRecruiter().getPhoneNumber(),
                        vacancy.getRecruiter().getEmail(), vacancy.getCompany(), vacancy.getProfession()));

                vacancy.getCandidateList().remove(vacancy.getCandidateList().get(i));
            }
        }
        vacancyRepository.save(vacancy);
        return new ResponseEntity<>(vacancy, HttpStatus.OK);
    }

    @GetMapping("/vacancys")
    public ResponseEntity<List<Vacancy>> getAllVacanciesForUser(Principal principal) {

        List<Vacancy> vacanciesList = userService.getAllVacancysForUser(principal);
        return new ResponseEntity<>(vacanciesList, HttpStatus.OK);
    }

    @GetMapping("/injectedVacancyForUser")
    public ResponseEntity<List<Vacancy>> getInjectedVacanciesForUser(Principal principal) {

        List<Vacancy> vacancyList = userService.getInjectedVacancysForUser(principal);
        return new ResponseEntity<>(vacancyList, HttpStatus.OK);
    }

    @GetMapping("/acceptedVacancyForUser")
    public ResponseEntity<List<Vacancy>> getAcceptedVacanciesForUser(Principal principal) {

        List<Vacancy> vacancyList = userService.getAcceptedVacancysForUser(principal);
        return new ResponseEntity<>(vacancyList, HttpStatus.OK);
    }

    @GetMapping("/vacancy/{id}")
    public ResponseEntity<Vacancy> getVacancyById(@PathVariable("id") Long id) {

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

        List<Vacancy> result = vacancyService.getVacancyListForUser(principal);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}