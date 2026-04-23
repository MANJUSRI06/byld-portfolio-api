package com.manju.byldportfolioapi.dto;

import com.manju.byldportfolioapi.enums.AlertKind;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreatePriceAlertRequest {

    @NotBlank
    private String symbol;

    @NotNull
    private AlertKind kind;

    @NotNull
    @DecimalMin(value = "0.0001", inclusive = true)
    private BigDecimal price;

    @NotBlank
    private String webhookUrl;
}