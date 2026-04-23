package com.manju.byldportfolioapi.repository;

import com.manju.byldportfolioapi.entity.Transaction;
import com.manju.byldportfolioapi.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByPortfolio(Portfolio portfolio);
}