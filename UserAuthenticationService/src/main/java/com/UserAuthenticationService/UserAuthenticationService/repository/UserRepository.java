package com.UserAuthenticationService.UserAuthenticationService.repository;

import com.UserAuthenticationService.UserAuthenticationService.entities.Role;
import com.UserAuthenticationService.UserAuthenticationService.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByEmail(String email);
    public User findByRole(Role role);
}
