package com.manju.byldportfolioapi.service;

import com.manju.byldportfolioapi.dto.CreatePriceAlertRequest;
import com.manju.byldportfolioapi.dto.PriceAlertResponse;
import com.manju.byldportfolioapi.entity.Portfolio;
import com.manju.byldportfolioapi.entity.PriceAlert;
import com.manju.byldportfolioapi.enums.AlertStatus;
import com.manju.byldportfolioapi.exception.ResourceNotFoundException;
import com.manju.byldportfolioapi.repository.PortfolioRepository;
import com.manju.byldportfolioapi.repository.PriceAlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PriceAlertService {

    private final PortfolioRepository portfolioRepository;
    private final PriceAlertRepository priceAlertRepository;

    public PriceAlertResponse createAlert(UUID portfolioId, CreatePriceAlertRequest request) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found with id: " + portfolioId));

        PriceAlert alert = PriceAlert.builder()
                .id(UUID.randomUUID())
                .portfolio(portfolio)
                .symbol(request.getSymbol().trim().toUpperCase())
                .kind(request.getKind())
                .targetPrice(request.getPrice())
                .webhookUrl(request.getWebhookUrl())
                .status(AlertStatus.ACTIVE)
                .firedAt(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        PriceAlert saved = priceAlertRepository.save(alert);

        return PriceAlertResponse.builder()
                .id(saved.getId())
                .portfolioId(saved.getPortfolio().getId())
                .symbol(saved.getSymbol())
                .kind(saved.getKind())
                .targetPrice(saved.getTargetPrice())
                .webhookUrl(saved.getWebhookUrl())
                .status(saved.getStatus())
                .firedAt(saved.getFiredAt())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    public java.util.List<PriceAlertResponse> getAlerts(UUID portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found with id: " + portfolioId));

        return priceAlertRepository.findByPortfolioId(portfolio.getId())
                .stream()
                .map(alert -> PriceAlertResponse.builder()
                        .id(alert.getId())
                        .portfolioId(alert.getPortfolio().getId())
                        .symbol(alert.getSymbol())
                        .kind(alert.getKind())
                        .targetPrice(alert.getTargetPrice())
                        .webhookUrl(alert.getWebhookUrl())
                        .status(alert.getStatus())
                        .firedAt(alert.getFiredAt())
                        .createdAt(alert.getCreatedAt())
                        .build())
                .toList();
    }
}