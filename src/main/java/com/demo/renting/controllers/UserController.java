package com.demo.renting.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.renting.payloads.ApiResponse;
import com.demo.renting.payloads.UserDto;
import com.demo.renting.services.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge=3600, allowCredentials="true" ,allowedHeaders = "*") 
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	//POST - for creating the user
	@PostMapping("/")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto)
	{
		
		String roleName = userDto.getRole();
		if(roleName==null) {
			userDto.setRole("customer");
		}
		
		
		UserDto createUSerDto = this.userService.createUser(userDto);
		return new ResponseEntity<>(createUSerDto, HttpStatus.CREATED);
	}
	
	//PUT - for updating the user
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto,@PathVariable("userId") Integer uid){
		UserDto updatedUser = this.userService.updateUser(userDto, uid);
		return ResponseEntity.ok(updatedUser);
	}
	
	
	//DELETE - for deleting the user
	//Only admin can delete the user.
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer uid){
		this.userService.deleteUser(uid);
		return new ResponseEntity<ApiResponse>(new ApiResponse("User deleted deleted successfully..!!!" , true), HttpStatus.OK);
	}
	
	//GET - to get the user
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers(){
		return ResponseEntity.ok(this.userService.getAllUsers());
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getSingleUser(@PathVariable Integer userId){
		return ResponseEntity.ok(this.userService.getUserById(userId));
	}
	
	
}
