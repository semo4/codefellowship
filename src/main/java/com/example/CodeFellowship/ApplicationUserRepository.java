package com.example.CodeFellowship;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Integer> {
    public ApplicationUser findByUsername(String userName);
}
