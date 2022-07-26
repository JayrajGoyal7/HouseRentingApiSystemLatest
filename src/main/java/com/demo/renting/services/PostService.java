package com.demo.renting.services;

import java.util.List;

import com.demo.renting.payloads.PostDto;
import com.demo.renting.payloads.PostResponse;

public interface PostService {

	//create
	PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);
	
	//update
	PostDto updatePost(PostDto postDto, Integer postId);
	
	//delete
	void deletePost(Integer postId);
	
	//get all posts
	PostResponse getAllPost(Integer pageNumner, Integer pageSize,String sortBy);
	
	//get single post 
	PostDto getPostById(Integer postId);
	
	//get all posts by category
	List<PostDto> getPostByCategory(Integer categoryId);
	
	//get all posts by user
	List<PostDto> getPostByUser(Integer userId);
	
	//search post according to keyword
	List<PostDto> searchPosts(String keyword);
	
	
	
	
	
	
	
}
