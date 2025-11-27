package com.example.week6.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.week6.blog.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
