package com.shpaginAS.recruiter.repository;

import com.shpaginAS.recruiter.models.User;
import com.shpaginAS.recruiter.models.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {

    List<Vacancy> findAllByRecruiterOrderById(User user);
}
