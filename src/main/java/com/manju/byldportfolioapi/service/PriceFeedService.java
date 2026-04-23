package com.manju.byldportfolioapi.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PriceFeedService {

    public BigDecimal getCurrentPrice(String symbol) {

        int hash = Math.abs(symbol.hashCode());

        double price = 1500 + (hash % 1000);

        return BigDecimal.valueOf(price)
                .setScale(2, RoundingMode.HALF_UP);
    }
}