package com.manju.byldportfolioapi.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class HoldingResponse {
    private UUID id;
    private String symbol;
    private BigDecimal quantity;
    private BigDecimal averageCost;
}