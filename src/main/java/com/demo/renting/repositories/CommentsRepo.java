package com.demo.renting.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import com.demo.renting.entities.Comment;

public interface CommentsRepo extends JpaRepository<Comment, Integer>{

}
