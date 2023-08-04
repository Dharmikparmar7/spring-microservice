package com.adiths.orderservice.orderservice.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Inventory {
    private String productId;

    private BigDecimal quantity; 
}

