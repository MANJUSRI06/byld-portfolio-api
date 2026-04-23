package com.manju.byldportfolioapi.controller;

import com.manju.byldportfolioapi.dto.CreatePriceAlertRequest;
import com.manju.byldportfolioapi.dto.PriceAlertResponse;
import com.manju.byldportfolioapi.service.PriceAlertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/portfolios/{id}/alerts")
@RequiredArgsConstructor
public class PriceAlertController {

    private final PriceAlertService priceAlertService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PriceAlertResponse createAlert(@PathVariable("id") UUID portfolioId,
                                          @Valid @RequestBody CreatePriceAlertRequest request) {
        return priceAlertService.createAlert(portfolioId, request);
    }

    @GetMapping
    public java.util.List<PriceAlertResponse> getAlerts(@PathVariable("id") UUID portfolioId) {
        return priceAlertService.getAlerts(portfolioId);
    }
}