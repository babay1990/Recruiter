package com.shpaginAS.recruiter.controllers;

import com.shpaginAS.recruiter.DTO.UserDTO;
import com.shpaginAS.recruiter.facade.UserFacade;
import com.shpaginAS.recruiter.models.User;
import com.shpaginAS.recruiter.payload.MessageResponse;
import com.shpaginAS.recruiter.repository.UserRepository;
import com.shpaginAS.recruiter.repository.VacancyRepository;
import com.shpaginAS.recruiter.services.UserService;
import com.shpaginAS.recruiter.validations.ResponceErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("api/user")

public class UserController {

    private final UserService userService;
    private final UserFacade userFacade;
    private final UserRepository userRepository;
    private final ResponceErrorValidation responceErrorValidation;

    @Autowired
    public UserController(UserService userService, UserFacade userFacade, UserRepository userRepository,
                          ResponceErrorValidation responceErrorValidation, VacancyRepository vacancyRepository){
        this.userService = userService;
        this.userFacade = userFacade;
        this.userRepository = userRepository;
        this.responceErrorValidation = responceErrorValidation;
    }

    @GetMapping("/")
    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {

        User user = userService.getCurrentUser(principal);
        UserDTO userDTO = userFacade.userToUserDTO(user);

        if(userDTO.getImageBytes() != null){
            userDTO.setImageBytes(userService.decompressBytes(userDTO.getImageBytes()));
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageToUser(@RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException {

        User user = userService.getCurrentUser(principal);
        user.setImageBytes(userService.compressBytes(file.getBytes()));
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Фотография успешно загружена!"));
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult, Principal principal) {
        ResponseEntity<Object> errors = responceErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        User user = userService.updateUser(userDTO, principal);
        UserDTO updatedUser = userFacade.userToUserDTO(user);

        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/details/{userId}")
    public ResponseEntity<UserDTO> userDetails(@PathVariable("userId") Long userId) {

        Optional<User> op = userRepository.findUserById(userId);
        User user = op.get();
        UserDTO userDTO = userFacade.userToUserDTO(user);

        if(userDTO.getImageBytes() != null){
            userDTO.setImageBytes(userService.decompressBytes(userDTO.getImageBytes()));
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PostMapping("/uploadSummary")
    public ResponseEntity<MessageResponse> uploadSummaryToUser(@RequestParam("file") MultipartFile file,
                                                             Principal principal) throws IOException {
        User user = userService.getCurrentUser(principal);
        user.setSummaryBytes(file.getBytes());
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Summary Uploaded Successfully"));
    }

    @GetMapping("/summary/{userId}")
    public ResponseEntity<byte[]> showSummary(@PathVariable("userId") Long userId) throws Exception {

        Optional<User> op = userRepository.findUserById(userId);
        User user = op.get();
        MediaType mediaType = MediaType.valueOf(MediaType.APPLICATION_PDF_VALUE);

        HttpHeaders respHeaders = new HttpHeaders();
        respHeaders.setContentLength(user.getSummaryBytes().length);
        respHeaders.setContentType(mediaType);
        respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + user.getLastname());

        return new ResponseEntity<>(user.getSummaryBytes(), respHeaders, HttpStatus.OK);
    }
}



