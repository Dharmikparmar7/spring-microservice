package com.adiths.productservice.productservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.adiths.productservice.productservice.dto.ProductRequest;
import com.adiths.productservice.productservice.dto.ProductResponse;
import com.adiths.productservice.productservice.service.ProductService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    
    @PostMapping
    @CircuitBreaker(name = "product", fallbackMethod = "fallbackMethod")
    public ResponseEntity<HttpStatus> createProduct(@RequestBody ProductRequest productRequest){
        return productService.createProduct(productRequest);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(){
        return productService.getAllProducts();
    }

    public String fallbackMethod(ProductRequest productRequest, RuntimeException runtimeException){
        return "Some error occurd !";
    }
}
