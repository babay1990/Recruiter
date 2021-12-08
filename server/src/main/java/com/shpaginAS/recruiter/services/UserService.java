package com.shpaginAS.recruiter.services;

import com.shpaginAS.recruiter.DTO.UserDTO;
import com.shpaginAS.recruiter.exeptions.UserExistException;
import com.shpaginAS.recruiter.models.User;
import com.shpaginAS.recruiter.models.Vacancy;
import com.shpaginAS.recruiter.models.enums.ERole;
import com.shpaginAS.recruiter.payload.request.SignupRequest;
import com.shpaginAS.recruiter.repository.UserRepository;
import com.shpaginAS.recruiter.repository.VacancyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final VacancyRepository vacancyRepository;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, VacancyRepository vacancyRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.vacancyRepository = vacancyRepository;
    }

    public User createUser(SignupRequest userIn){
        User user = new User();
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setRole(userIn.getRole());
        user.setPassword(bCryptPasswordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(ERole.ROLE_USER);

        try {
            LOG.info("Saving User {}", userIn.getEmail());
            return userRepository.save(user);
        } catch (Exception ex) {
            LOG.error("Error during registration {}", ex.getMessage());
            throw new UserExistException("The user " + user.getUsername() + " already exist. Please check credentials");
        }
    }

    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));

    }

    public byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            LOG.error("Cannot compress Bytes");
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    public byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            LOG.error("Cannot decompress Bytes");
        }
        return outputStream.toByteArray();
    }

    public User updateUser(UserDTO userDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        user.setName(userDTO.getName());
        user.setLastname(userDTO.getLastname());
        user.setProfession(userDTO.getProfession());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        return userRepository.save(user);
    }

    public List<Vacancy> getAllVacancysForUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        if(user.getRole().equals("Рекрутер")){
            return vacancyRepository.findAllByRecruiterOrderById(user);
        }else {

            List<Vacancy> result = new ArrayList<>();
            List<Vacancy> list = vacancyRepository.findAll();

            for(Vacancy vacancy : list){
                if(vacancy.getCandidateList().contains(user) || vacancy.getInjectedCandidateList().contains(user) || vacancy.getApprovedCandidateList().contains(user)){
                    result.add(vacancy);
                }
            }
            return result;
        }
    }

    public List<Vacancy> getInjectedVacancysForUser(Principal principal) {
        User user = getUserByPrincipal(principal);

            List<Vacancy> result = new ArrayList<>();
            List<Vacancy> list = vacancyRepository.findAll();

            for(Vacancy vacancy : list){
                if(vacancy.getInjectedCandidateList().contains(user)){
                    result.add(vacancy);
                }
            }
            return result;
    }

    public List<Vacancy> getAcceptedVacancysForUser(Principal principal) {
        User user = getUserByPrincipal(principal);

        List<Vacancy> result = new ArrayList<>();
        List<Vacancy> list = vacancyRepository.findAll();

        for(Vacancy vacancy : list){
            if(vacancy.getApprovedCandidateList().contains(user)){
                result.add(vacancy);
            }
        }
        return result;
    }
}
