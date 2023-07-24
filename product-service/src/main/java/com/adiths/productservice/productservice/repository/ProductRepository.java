package com.adiths.productservice.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.adiths.productservice.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String>{
    
}
