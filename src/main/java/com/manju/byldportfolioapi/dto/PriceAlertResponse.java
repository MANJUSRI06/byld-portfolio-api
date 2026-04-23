package com.manju.byldportfolioapi.dto;

import com.manju.byldportfolioapi.enums.AlertKind;
import com.manju.byldportfolioapi.enums.AlertStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class PriceAlertResponse {
    private UUID id;
    private UUID portfolioId;
    private String symbol;
    private AlertKind kind;
    private BigDecimal targetPrice;
    private String webhookUrl;
    private AlertStatus status;
    private LocalDateTime firedAt;
    private LocalDateTime createdAt;
}