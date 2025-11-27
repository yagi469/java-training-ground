package com.example.week4.finaltask.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.week4.finaltask.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
