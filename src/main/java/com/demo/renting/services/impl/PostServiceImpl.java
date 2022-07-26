package com.demo.renting.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.demo.renting.entities.Category;
import com.demo.renting.entities.Post;
import com.demo.renting.entities.User;
import com.demo.renting.exceptions.ResourceNotFoundException;
import com.demo.renting.payloads.PostDto;
import com.demo.renting.payloads.PostResponse;
import com.demo.renting.repositories.CategoryRepo;
import com.demo.renting.repositories.PostRepo;
import com.demo.renting.repositories.UserRepo;
import com.demo.renting.services.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	
	// create the new post
	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", " User Id ", userId));

		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", " Category Id ", categoryId));
		Post post = this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());

		post.setUser(user);
		post.setCategory(category);

		Post newPost = this.postRepo.save(post);

		return this.modelMapper.map(newPost, PostDto.class);
	}
	
	//updating the post.

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {

		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", " Post Id ", postId));

		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());

		Post updatedPost = this.postRepo.save(post);
		return this.modelMapper.map(updatedPost, PostDto.class);
	}

	// Deleting the post.
	@Override
	public void deletePost(Integer postId) {

		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", " Post Id ", postId));
		this.postRepo.delete(post);

	}
	
	//Get all the psots.

	@Override
	public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy) {

		// int pageSize=5;
		// int pageNumber=1;

		//Sort sort = sortDir.equalsIgnoreCase("ASC")?Sort.by(sortDir).ascending():Sort.by(sortDir).descending();

		Pageable p = PageRequest.of(pageNumber, pageSize);

		Page<Post> pagePosts = this.postRepo.findAll(p);

		List<Post> allPosts = pagePosts.getContent();

		List<PostDto> postDtos = allPosts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		PostResponse postResponse = new PostResponse();
		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePosts.getNumber());
		postResponse.setPageSize(pagePosts.getSize());
		postResponse.setTotalElements(pagePosts.getTotalElements());
		postResponse.setTotalPages(pagePosts.getTotalPages());
		postResponse.setLastPage(pagePosts.isLast());

		return postResponse;
	}

	
	//Get post by user id.
	@Override
	public PostDto getPostById(Integer postId) {

		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post", " Post Id ", postId));

		return this.modelMapper.map(post, PostDto.class);
	}

	// get post by category id.
	@Override
	public List<PostDto> getPostByCategory(Integer categoryId) {

		Category cat = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", " Category Id ", categoryId));
		List<Post> posts = this.postRepo.findByCategory(cat);

		List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());

		return postDtos;
	}
	
	//get post by user id.

	@Override
	public List<PostDto> getPostByUser(Integer userId) {

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", " User Id ", userId));

		List<Post> posts = this.postRepo.findByUser(user);

		List<PostDto> postDtos = posts.stream().map((post) -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		return postDtos;
	}

	// searching inside the post.
	@Override
	public List<PostDto> searchPosts(String keyword) {

		List<Post> posts = this.postRepo.findByTitleContaining(keyword);

		List<PostDto> postDtos = posts.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		
		return postDtos;
	}
	

}
