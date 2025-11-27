package com.example.week6.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.week6.blog.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
