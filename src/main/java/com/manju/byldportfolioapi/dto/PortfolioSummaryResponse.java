package com.manju.byldportfolioapi.dto;

import com.manju.byldportfolioapi.enums.RiskProfile;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class PortfolioSummaryResponse {
    private UUID id;
    private String clientName;
    private RiskProfile riskProfile;
    private BigDecimal cashBalance;
    private List<HoldingResponse> holdings;
}