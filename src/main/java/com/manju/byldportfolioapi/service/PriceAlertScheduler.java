package com.manju.byldportfolioapi.service;

import com.manju.byldportfolioapi.entity.PriceAlert;
import com.manju.byldportfolioapi.enums.AlertKind;
import com.manju.byldportfolioapi.enums.AlertStatus;
import com.manju.byldportfolioapi.repository.PriceAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceAlertScheduler {

    private final PriceAlertRepository priceAlertRepository;
    private final PriceFeedService priceFeedService;

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 30000)
    public void checkAlerts() {

        List<PriceAlert> activeAlerts =
                priceAlertRepository.findByStatus(AlertStatus.ACTIVE);

        for (PriceAlert alert : activeAlerts) {

            BigDecimal currentPrice =
                    priceFeedService.getCurrentPrice(alert.getSymbol());

            boolean triggered = false;

            if (alert.getKind() == AlertKind.ABOVE &&
                    currentPrice.compareTo(alert.getTargetPrice()) > 0) {

                triggered = true;
            }

            if (alert.getKind() == AlertKind.BELOW &&
                    currentPrice.compareTo(alert.getTargetPrice()) < 0) {

                triggered = true;
            }

            if (triggered) {

                sendWebhook(alert, currentPrice);

                alert.setStatus(AlertStatus.INACTIVE);
                alert.setFiredAt(LocalDateTime.now());
                alert.setUpdatedAt(LocalDateTime.now());

                priceAlertRepository.save(alert);

                log.info("Alert triggered for {}", alert.getSymbol());
            }
        }
    }

    private void sendWebhook(PriceAlert alert, BigDecimal currentPrice) {

        Map<String, Object> payload = new HashMap<>();

        payload.put("alertId", alert.getId());
        payload.put("symbol", alert.getSymbol());
        payload.put("kind", alert.getKind());
        payload.put("targetPrice", alert.getTargetPrice());
        payload.put("currentPrice", currentPrice);
        payload.put("triggeredAt", LocalDateTime.now());

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(payload, headers);

        restTemplate.postForEntity(
                alert.getWebhookUrl(),
                request,
                String.class
        );
    }
}