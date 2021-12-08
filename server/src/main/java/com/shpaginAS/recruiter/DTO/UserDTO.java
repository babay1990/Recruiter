package com.shpaginAS.recruiter.DTO;


import com.shpaginAS.recruiter.models.Vacancy;

import java.util.ArrayList;

public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private String lastname;
    private String profession;
    private String phoneNumber;
    private byte[] imageBytes;
    private String role;
    private byte[] summaryBytes;

    private ArrayList<Vacancy> recruiterVacancyList;

    private ArrayList<Vacancy> employeeVacancyList;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phonenumber) {
        this.phoneNumber = phonenumber;
    }

    public ArrayList<Vacancy> getRecruiterVacancyList() {
        return recruiterVacancyList;
    }

    public void setRecruiterVacancyList(ArrayList<Vacancy> recruiterVacancyList) {
        this.recruiterVacancyList = recruiterVacancyList;
    }

    public ArrayList<Vacancy> getEmployeeVacancyList() {
        return employeeVacancyList;
    }

    public void setEmployeeVacancyList(ArrayList<Vacancy> employeeVacancyList) {
        this.employeeVacancyList = employeeVacancyList;
    }

    public byte[] getSummaryBytes() {
        return summaryBytes;
    }

    public void setSummaryBytes(byte[] summaryBytes) {
        this.summaryBytes = summaryBytes;
    }
}
