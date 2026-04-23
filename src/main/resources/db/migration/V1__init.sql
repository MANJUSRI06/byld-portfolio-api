CREATE TABLE portfolios (
                            id UUID PRIMARY KEY,
                            client_name VARCHAR(255) NOT NULL,
                            risk_profile VARCHAR(50) NOT NULL,
                            cash_balance NUMERIC(19,4) NOT NULL DEFAULT 0.0000,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE holdings (
                          id UUID PRIMARY KEY,
                          portfolio_id UUID NOT NULL,
                          symbol VARCHAR(20) NOT NULL,
                          quantity NUMERIC(19,4) NOT NULL,
                          average_cost NUMERIC(19,4) NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_holdings_portfolio
                              FOREIGN KEY (portfolio_id) REFERENCES portfolios(id) ON DELETE CASCADE,
                          CONSTRAINT uk_portfolio_symbol UNIQUE (portfolio_id, symbol)
);

CREATE TABLE transactions (
                              id UUID PRIMARY KEY,
                              portfolio_id UUID NOT NULL,
                              symbol VARCHAR(20) NOT NULL,
                              type VARCHAR(10) NOT NULL,
                              quantity NUMERIC(19,4) NOT NULL,
                              price NUMERIC(19,4) NOT NULL,
                              total_amount NUMERIC(19,4) NOT NULL,
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT fk_transactions_portfolio
                                  FOREIGN KEY (portfolio_id) REFERENCES portfolios(id) ON DELETE CASCADE
);

CREATE TABLE price_alerts (
                              id UUID PRIMARY KEY,
                              portfolio_id UUID NOT NULL,
                              symbol VARCHAR(20) NOT NULL,
                              kind VARCHAR(10) NOT NULL,
                              target_price NUMERIC(19,4) NOT NULL,
                              webhook_url VARCHAR(500) NOT NULL,
                              status VARCHAR(20) NOT NULL,
                              fired_at TIMESTAMP NULL,
                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT fk_alerts_portfolio
                                  FOREIGN KEY (portfolio_id) REFERENCES portfolios(id) ON DELETE CASCADE
);