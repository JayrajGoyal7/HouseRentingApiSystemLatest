package com.demo.renting.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.renting.payloads.ApiResponse;
import com.demo.renting.payloads.CategoryDto;
import com.demo.renting.payloads.UserDto;
import com.demo.renting.services.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	// create
	@PostMapping("/")
	public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
		CategoryDto createCategory = this.categoryService.createCategory(categoryDto);

		return new ResponseEntity<CategoryDto>(createCategory, HttpStatus.CREATED);
	}

	// update
	@PutMapping("/{catId}")
	public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,
			@PathVariable Integer catId) {
		CategoryDto updatedCategory = this.categoryService.updateCategory(categoryDto, catId);

		return new ResponseEntity<CategoryDto>(updatedCategory, HttpStatus.OK);
	}

	// delete
	@DeleteMapping("/{catId}")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Integer catId) {
		this.categoryService.deleteCategory(catId);

		return new ResponseEntity<ApiResponse>(new ApiResponse("Category is deleted successfully...!!",true), HttpStatus.OK);
	}

	//get category
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getSingleUser(@PathVariable Integer categoryId){
		return ResponseEntity.ok(this.categoryService.getCategory(categoryId));
	}
	
	// get all categories
	@GetMapping("/")
	public ResponseEntity<List<CategoryDto>> getAllUsers(){
		return ResponseEntity.ok(this.categoryService.getCategories());
	}
	
	
	
}
