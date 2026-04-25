package com.manju.byldportfolioapi.service;

import com.manju.byldportfolioapi.dto.CreatePortfolioRequest;
import com.manju.byldportfolioapi.dto.TransactionRequest;
import com.manju.byldportfolioapi.entity.Holding;
import com.manju.byldportfolioapi.entity.Portfolio;
import com.manju.byldportfolioapi.entity.Transaction;
import com.manju.byldportfolioapi.enums.TransactionType;
import com.manju.byldportfolioapi.exception.ResourceNotFoundException;
import com.manju.byldportfolioapi.repository.HoldingRepository;
import com.manju.byldportfolioapi.repository.PortfolioRepository;
import com.manju.byldportfolioapi.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.manju.byldportfolioapi.exception.InsufficientHoldingException;
import com.manju.byldportfolioapi.dto.HoldingResponse;
import com.manju.byldportfolioapi.dto.PortfolioSummaryResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final HoldingRepository holdingRepository;
    private final TransactionRepository transactionRepository;
    private final PriceFeedService priceFeedService;

    public Portfolio createPortfolio(CreatePortfolioRequest request) {
        Portfolio portfolio = Portfolio.builder()
                .id(UUID.randomUUID())
                .clientName(request.getClientName())
                .riskProfile(request.getRiskProfile())
                .cashBalance(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return portfolioRepository.save(portfolio);
    }

    @Transactional
    public Transaction buyTransaction(UUID portfolioId, TransactionRequest request) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found with id: " + portfolioId));

        String symbol = request.getSymbol().trim().toUpperCase();
        BigDecimal buyQuantity = request.getQuantity();
        BigDecimal buyPrice = request.getPrice();
        BigDecimal totalAmount = buyQuantity.multiply(buyPrice);

        Holding holding = holdingRepository.findByPortfolioAndSymbol(portfolio, symbol)
                .orElse(null);

        if (holding == null) {
            holding = Holding.builder()
                    .id(UUID.randomUUID())
                    .portfolio(portfolio)
                    .symbol(symbol)
                    .quantity(buyQuantity)
                    .averageCost(buyPrice.setScale(4, RoundingMode.HALF_UP))
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        } else {
            BigDecimal oldQuantity = holding.getQuantity();
            BigDecimal oldAverageCost = holding.getAverageCost();

            BigDecimal oldTotalCost = oldQuantity.multiply(oldAverageCost);
            BigDecimal newBuyCost = buyQuantity.multiply(buyPrice);
            BigDecimal newTotalQuantity = oldQuantity.add(buyQuantity);

            BigDecimal newAverageCost = oldTotalCost.add(newBuyCost)
                    .divide(newTotalQuantity, 4, RoundingMode.HALF_UP);

            holding.setQuantity(newTotalQuantity);
            holding.setAverageCost(newAverageCost);
            holding.setUpdatedAt(LocalDateTime.now());
        }

        holdingRepository.save(holding);

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .portfolio(portfolio)
                .symbol(symbol)
                .type(TransactionType.BUY)
                .quantity(buyQuantity)
                .price(buyPrice)
                .totalAmount(totalAmount.setScale(4, RoundingMode.HALF_UP))
                .createdAt(LocalDateTime.now())
                .build();

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction sellTransaction(UUID portfolioId, TransactionRequest request) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found with id: " + portfolioId));

        String symbol = request.getSymbol().trim().toUpperCase();
        BigDecimal sellQuantity = request.getQuantity();
        BigDecimal sellPrice = request.getPrice();
        BigDecimal totalAmount = sellQuantity.multiply(sellPrice);

        Holding holding = holdingRepository.findByPortfolioAndSymbol(portfolio, symbol)
                .orElseThrow(() -> new ResourceNotFoundException("Holding not found for symbol: " + symbol));

        if (holding.getQuantity().compareTo(sellQuantity) < 0) {
            throw new InsufficientHoldingException("Cannot sell " + sellQuantity +
                    " units of " + symbol + ". Available quantity: " + holding.getQuantity());
        }

        BigDecimal remainingQuantity = holding.getQuantity().subtract(sellQuantity);

        if (remainingQuantity.compareTo(BigDecimal.ZERO) == 0) {
            holdingRepository.delete(holding);
        } else {
            holding.setQuantity(remainingQuantity);
            holding.setUpdatedAt(LocalDateTime.now());
            holdingRepository.save(holding);
        }

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .portfolio(portfolio)
                .symbol(symbol)
                .type(TransactionType.SELL)
                .quantity(sellQuantity)
                .price(sellPrice)
                .totalAmount(totalAmount.setScale(4, RoundingMode.HALF_UP))
                .createdAt(LocalDateTime.now())
                .build();

        return transactionRepository.save(transaction);
    }

    public List<Holding> getHoldings(UUID portfolioId) {

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Portfolio not found with id: " + portfolioId
                ));

        return holdingRepository.findByPortfolio(portfolio);
    }

    public PortfolioSummaryResponse getPortfolioSummary(UUID portfolioId) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ResourceNotFoundException("Portfolio not found with id: " + portfolioId));

        List<HoldingResponse> holdings = holdingRepository.findByPortfolio(portfolio)
                .stream()
                .map(holding -> {
                    BigDecimal currentPrice = priceFeedService.getCurrentPrice(holding.getSymbol());

                    BigDecimal investedValue =
                            holding.getQuantity().multiply(holding.getAverageCost());

                    BigDecimal marketValue =
                            holding.getQuantity().multiply(currentPrice);

                    BigDecimal profitLoss =
                            marketValue.subtract(investedValue);

                    return HoldingResponse.builder()
                            .id(holding.getId())
                            .symbol(holding.getSymbol())
                            .quantity(holding.getQuantity())
                            .averageCost(holding.getAverageCost())
                            .currentPrice(currentPrice)
                            .investedValue(investedValue)
                            .marketValue(marketValue)
                            .profitLoss(profitLoss)
                            .build();
                })
                .toList();

        return PortfolioSummaryResponse.builder()
                .id(portfolio.getId())
                .clientName(portfolio.getClientName())
                .riskProfile(portfolio.getRiskProfile())
                .cashBalance(portfolio.getCashBalance())
                .holdings(holdings)
                .build();
    }
}