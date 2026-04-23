package com.manju.byldportfolioapi.repository;

import com.manju.byldportfolioapi.entity.PriceAlert;
import com.manju.byldportfolioapi.enums.AlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, UUID> {
    List<PriceAlert> findByStatus(AlertStatus status);
    List<PriceAlert> findByPortfolioId(UUID portfolioId);
}