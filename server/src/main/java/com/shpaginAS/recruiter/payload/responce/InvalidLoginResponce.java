package com.shpaginAS.recruiter.payload.responce;


public class InvalidLoginResponce {
   private String username;
   private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public InvalidLoginResponce(){
        this.username = "invalid username";
        this.password = "invalid password";
    }
}
