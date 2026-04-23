package com.manju.byldportfolioapi.repository;

import com.manju.byldportfolioapi.entity.Holding;
import com.manju.byldportfolioapi.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HoldingRepository extends JpaRepository<Holding, UUID> {
    List<Holding> findByPortfolio(Portfolio portfolio);
    Optional<Holding> findByPortfolioAndSymbol(Portfolio portfolio, String symbol);
}