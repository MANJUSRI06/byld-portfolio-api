package com.manju.byldportfolioapi.controller;

import com.manju.byldportfolioapi.dto.CreatePortfolioRequest;
import com.manju.byldportfolioapi.dto.TransactionRequest;
import com.manju.byldportfolioapi.entity.Holding;
import com.manju.byldportfolioapi.entity.Portfolio;
import com.manju.byldportfolioapi.entity.Transaction;
import com.manju.byldportfolioapi.service.PortfolioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.manju.byldportfolioapi.dto.PortfolioSummaryResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/portfolios")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Portfolio createPortfolio(@Valid @RequestBody CreatePortfolioRequest request) {
        return portfolioService.createPortfolio(request);
    }

    @PostMapping("/{id}/transactions/buy")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction buyTransaction(@PathVariable UUID id,
                                      @Valid @RequestBody TransactionRequest request) {
        return portfolioService.buyTransaction(id, request);
    }

    @GetMapping("/{id}/holdings")
    public List<Holding> getHoldings(@PathVariable UUID id) {
        return portfolioService.getHoldings(id);
    }

    @PostMapping("/{id}/transactions/sell")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction sellTransaction(@PathVariable UUID id,
                                       @Valid @RequestBody TransactionRequest request) {
        return portfolioService.sellTransaction(id, request);
    }

    @GetMapping("/{id}")
    public PortfolioSummaryResponse getPortfolioSummary(@PathVariable UUID id) {
        return portfolioService.getPortfolioSummary(id);
    }
}