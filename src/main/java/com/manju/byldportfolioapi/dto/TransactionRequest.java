package com.manju.byldportfolioapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionRequest {

    @NotBlank
    private String symbol;

    @NotNull
    @DecimalMin(value = "0.0001", inclusive = true)
    private BigDecimal quantity;

    @NotNull
    @DecimalMin(value = "0.0001", inclusive = true)
    private BigDecimal price;
}