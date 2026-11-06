package by.kovalevskiy.BinanceAvrPrice.scheduler;

import by.kovalevskiy.BinanceAvrPrice.models.BinanceResponse;
import by.kovalevskiy.BinanceAvrPrice.models.PairPrice;
import by.kovalevskiy.BinanceAvrPrice.models.Symbol;
import by.kovalevskiy.BinanceAvrPrice.repositories.PairPriceRepository;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.time.Instant;

@Component
@Slf4j
public class BinanceScheduler {
    private final PairPriceRepository repository;
    private final RestClient restClient;

    public BinanceScheduler(PairPriceRepository repository, @Value("${binance.api.base-url}") String baseUrl) {
        this.repository = repository;
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    @Scheduled(fixedRateString = "${binance.poll-interval}")
    public void PriceFromBinance() {
        log.info("Планировщик запущен");
        String symbol = "BNBUSDT";
        try {
            log.info("Запрашиваем данные у Binance");
            BinanceResponse response = restClient.get()
                    .uri("/api/v3/avgPrice?symbol=" + symbol)
                    .retrieve()
                    .body(BinanceResponse.class);

            if (response != null && response.price() != null) {
                PairPrice pairPrice = new PairPrice(Symbol.valueOf(symbol), response.price(), Instant.now());
                repository.save(pairPrice);
                log.info("Данные получены и сохранены в базу данных: {}", response.price());
            }
        } catch (Exception e) {
            log.error("Ошибка при запросе к Binance API: {}. Расписание не останавливается.", e.getMessage());
        }
    }
}
