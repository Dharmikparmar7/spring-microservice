package com.adiths.productservice.productservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.adiths.productservice.productservice.dto.InventoryRequest;
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
    private final WebClient.Builder webClientBuilder;

    public ResponseEntity<HttpStatus> createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);

        InventoryRequest inventoryRequest = InventoryRequest.builder()
                .productId(product.getId())
                .quantity(productRequest.getQuantity()).build();

        webClientBuilder.build().post()
                .uri("http://inventory-service/api/inventory")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(inventoryRequest), InventoryRequest.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return new ResponseEntity<>(products.stream().map(this::mapProductResponse).toList(), HttpStatus.OK);
    }

    private ProductResponse mapProductResponse(Product product) {
        return ProductResponse.builder()
                .Id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
