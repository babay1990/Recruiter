package com.shpaginAS.recruiter.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shpaginAS.recruiter.models.enums.ERole;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name="\"user\"")
public class User implements UserDetails, Serializable {

    private static final long serialVersionUID = -6884218167712683803L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String lastname;
    @Column(unique = true)
    private String email;

    private String phoneNumber;
    @Column(unique = true, updatable = false)
    private String username;
    @Column(length = 3000)
    private String password;

    @Column(columnDefinition = "bytea")
    private byte[] imageBytes;

    @Column(columnDefinition = "bytea")
    private byte[] summaryBytes;

    @Column(nullable = false)
    private String role;

    @ElementCollection(targetClass = ERole.class)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    @Fetch(FetchMode.JOIN)
    private Set<ERole> roles = new HashSet<>();

    private String profession;
    private ArrayList<Vacancy> recruiterVacancyList;
    private ArrayList<Vacancy> employeeVacancyList;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    private boolean isRegistered;

    public User() {
    }

    public User(long id, String name, String lastname, String email, String phoneNumber, String username, String password, Set<ERole> roles, String profession, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.profession = profession;
        this.authorities = authorities;
    }

    public User(long id, String name, String lastname, String email, String phoneNumber, String username, String password, Set<ERole> roles, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.authorities = authorities;
    }

    public User(Long id,
                String username,
                String email,
                String password,
                Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<ERole> getRoles() {
        return roles;
    }

    public void setRoles(Set<ERole> roles) {
        this.roles = roles;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public byte[] getSummaryBytes() {
        return summaryBytes;
    }

    public void setSummaryBytes(byte[] summaryBytes) {
        this.summaryBytes = summaryBytes;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) return true;
        if(o == null || o.getClass() != this.getClass()) return false;

        User user = (User) o;
        return getEmail().equals(user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }
}
