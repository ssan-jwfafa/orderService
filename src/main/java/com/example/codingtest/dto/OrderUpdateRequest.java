package com.example.codingtest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record OrderUpdateRequest(
        @NotNull Long userId,
        @Valid @NotEmpty List<OrderItemRequest> items
) {
}
