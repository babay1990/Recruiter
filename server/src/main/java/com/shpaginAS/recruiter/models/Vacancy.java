package com.shpaginAS.recruiter.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;


@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name = "vacancy")
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User recruiter;

    private String company;
    private String profession;

    @Column(columnDefinition = "text")
    private String description;

    private String adress;
    private ArrayList<User> candidateList;
    private ArrayList<User> injectedCandidateList;
    private ArrayList<User> approvedCandidateList;

    public Vacancy() {
    }

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
