package com.shpaginAS.recruiter.DTO;

import com.shpaginAS.recruiter.models.User;

import java.util.ArrayList;

public class VacancyDTO {

    private long id;
    private User recruiter;
    private String company;
    private String profession;
    private String description;
    private String adress;
    private ArrayList<User> candidateList;
    private ArrayList<User> injectedCandidateList;
    private ArrayList<User> approvedCandidateList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(User recruiter) {
        this.recruiter = recruiter;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public ArrayList<User> getCandidateList() {
        return candidateList;
    }

    public void setCandidateList(ArrayList<User> candidateList) {
        this.candidateList = candidateList;
    }

    public ArrayList<User> getInjectedCandidateList() {
        return injectedCandidateList;
    }

    public void setInjectedCandidateList(ArrayList<User> injectedCandidateList) {
        this.injectedCandidateList = injectedCandidateList;
    }

    public ArrayList<User> getApprovedCandidateList() {
        return approvedCandidateList;
    }

    public void setApprovedCandidateList(ArrayList<User> approvedCandidateList) {
        this.approvedCandidateList = approvedCandidateList;
    }
}
