package com.adiths.productservice.productservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.adiths.productservice.productservice.dto.ProductRequest;
import com.adiths.productservice.productservice.dto.ProductResponse;
import com.adiths.productservice.productservice.model.Product;
import com.adiths.productservice.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;


    @Autowired
    private final WebClient webClient;

    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
        .name(productRequest.getName())
        .description(productRequest.getDescription())
        .price(productRequest.getPrice())
        .build();

        productRepository.save(product);
        log.info("Product with name : {} Created, {}", product.getName(), product.getId());

        productRequest.setProductId(product.getId());

        log.info("productrequest = {}", (productRequest.getProductId()));

        webClient.post()
            .uri("http://localhost:8081/api/inventory")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(productRequest), ProductRequest.class)
            .retrieve()
            .bodyToMono(Void.class)
            .block();

        log.info("Inventory added");
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapProductResponse).toList();
    }

    private ProductResponse mapProductResponse(Product product){
        return ProductResponse.builder()
            .Id(product.getId())
            .name(product.getName())
            .description(product.getDescription())
            .price(product.getPrice())
            .build();
    }
}
