package com.shpaginAS.recruiter.controllers;

import com.shpaginAS.recruiter.models.User;
import com.shpaginAS.recruiter.payload.MessageResponse;
import com.shpaginAS.recruiter.payload.request.LoginRequest;
import com.shpaginAS.recruiter.payload.request.SignupRequest;
import com.shpaginAS.recruiter.payload.responce.JWTTokenSucceddResponse;
import com.shpaginAS.recruiter.repository.UserRepository;
import com.shpaginAS.recruiter.security.JWTTokenProvider;
import com.shpaginAS.recruiter.security.SecurityConstants;
import com.shpaginAS.recruiter.services.KafkaProducerService;
import com.shpaginAS.recruiter.services.UserService;
import com.shpaginAS.recruiter.validations.ResponceErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {

    @Autowired
    private ResponceErrorValidation responceErrorValidation;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private KafkaProducerService producerService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signin")
    public ResponseEntity<Object> logIn(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responceErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors;


        Optional<User> op = userRepository.findUserByEmail(loginRequest.getUsername());
        User user = op.get();

        if(user.isRegistered()){
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
            ));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);

            return ResponseEntity.ok(new JWTTokenSucceddResponse(true, jwt));
        } else {
            return new ResponseEntity<>("Учетная запись не подтверждена", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> userRegistration(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult) {
        ResponseEntity<Object> errors = responceErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors;

        userService.createUser(signupRequest);
        producerService.sendEmailForAcceptRegistration(signupRequest.getEmail());

        return ResponseEntity.ok(new MessageResponse("Пользователь успешно зарегестрирован!"));
    }

    @GetMapping("/acceptRegistration")
    public String acceptRegistration(@RequestParam String email){
        Optional<User> op = userRepository.findUserByEmail(email);
        User user = op.get();

        user.setRegistered(true);
        userRepository.save(user);

        return "Подтверждение регистрации прошло успешно!";

    }
}
