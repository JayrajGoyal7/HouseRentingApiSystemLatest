package com.demo.renting.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.renting.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer>{

	
	
	
}
