package com.nagarro.miniassignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nagarro.miniassignment.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
	@Query("SELECT COUNT(u) FROM User u")
    long getTotalCount();
}
