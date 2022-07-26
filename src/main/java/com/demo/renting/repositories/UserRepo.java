package com.demo.renting.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.renting.entities.User;

public interface UserRepo extends JpaRepository<User, Integer>{

	
	Optional<User> findByEmail(String email);
}
