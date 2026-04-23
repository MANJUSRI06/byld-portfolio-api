package com.manju.byldportfolioapi.dto;

import com.manju.byldportfolioapi.enums.RiskProfile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePortfolioRequest {

    @NotBlank
    private String clientName;

    @NotNull
    private RiskProfile riskProfile;
}