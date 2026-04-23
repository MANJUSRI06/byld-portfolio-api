package com.manju.byldportfolioapi.repository;

import com.manju.byldportfolioapi.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {
}