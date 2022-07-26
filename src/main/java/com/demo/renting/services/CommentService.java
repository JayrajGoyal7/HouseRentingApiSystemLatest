package com.demo.renting.services;


import com.demo.renting.payloads.CommentDto;

public interface CommentService {

	
	CommentDto createComment(CommentDto commentDto, Integer postId);
	
	void deleteComment(Integer commentId);
	
}
