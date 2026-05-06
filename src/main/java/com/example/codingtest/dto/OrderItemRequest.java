package com.example.codingtest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OrderItemRequest(
        @NotBlank @Size(max = 100) String productName,
        @Min(0) int price,
        @Min(1) int quantity
) {
}
