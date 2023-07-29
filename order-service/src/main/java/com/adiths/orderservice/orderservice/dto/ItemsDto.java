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
public class ItemsDto {
    private String productId;

    private BigDecimal price;
    
    private BigDecimal quantity;
}
