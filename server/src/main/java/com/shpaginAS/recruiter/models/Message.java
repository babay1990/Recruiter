package com.shpaginAS.recruiter.models;

public class Message {

    private String candidateEmail;
    private String candidateFirstname;

    private String recruiterFirstname;
    private String recruiterLastname;
    private String recruiterPhoneNumber;
    private String recruiterEmail;

    private String vacancyCompany;
    private String vacancyProfession;

    public Message(String candidateEmail, String candidateFirstname, String recruiterFirstname,
                   String recruiterLastname, String recruiterPhoneNumber, String recruiterEmail,
                   String vacancyCompany, String vacancyProfession) {
        this.candidateEmail = candidateEmail;
        this.candidateFirstname = candidateFirstname;
        this.recruiterFirstname = recruiterFirstname;
        this.recruiterLastname = recruiterLastname;
        this.recruiterPhoneNumber = recruiterPhoneNumber;
        this.recruiterEmail = recruiterEmail;
        this.vacancyCompany = vacancyCompany;
        this.vacancyProfession = vacancyProfession;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    public String getCandidateFirstname() {
        return candidateFirstname;
    }

    public void setCandidateFirstname(String candidateFirstname) {
        this.candidateFirstname = candidateFirstname;
    }

    public String getRecruiterFirstname() {
        return recruiterFirstname;
    }

    public void setRecruiterFirstname(String recruiterFirstname) {
        this.recruiterFirstname = recruiterFirstname;
    }

    public String getRecruiterLastname() {
        return recruiterLastname;
    }

    public void setRecruiterLastname(String recruiterLastname) {
        this.recruiterLastname = recruiterLastname;
    }

    public String getRecruiterPhoneNumber() {
        return recruiterPhoneNumber;
    }

    public void setRecruiterPhoneNumber(String recruiterPhoneNumber) {
        this.recruiterPhoneNumber = recruiterPhoneNumber;
    }

    public String getRecruiterEmail() {
        return recruiterEmail;
    }

    public void setRecruiterEmail(String recruiterEmail) {
        this.recruiterEmail = recruiterEmail;
    }

    public String getVacancyCompany() {
        return vacancyCompany;
    }

    public void setVacancyCompany(String vacancyCompany) {
        this.vacancyCompany = vacancyCompany;
    }

    public String getVacancyProfession() {
        return vacancyProfession;
    }

    public void setVacancyProfession(String vacancyProfession) {
        this.vacancyProfession = vacancyProfession;
    }
}
