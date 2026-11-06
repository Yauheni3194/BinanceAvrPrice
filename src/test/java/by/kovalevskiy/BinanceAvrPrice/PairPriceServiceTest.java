package by.kovalevskiy.BinanceAvrPrice;

import by.kovalevskiy.BinanceAvrPrice.DTO.TrendResponseDto;
import by.kovalevskiy.BinanceAvrPrice.models.PairPrice;
import by.kovalevskiy.BinanceAvrPrice.models.Symbol;
import by.kovalevskiy.BinanceAvrPrice.repositories.PairPriceRepository;
import by.kovalevskiy.BinanceAvrPrice.services.PairPriceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PairPriceServiceTest {
    @Mock
    private PairPriceRepository repository;

    @InjectMocks
    private PairPriceService service;

    @Test
    void shouldCalculateUpwardTrend() {
        Symbol symbol = Symbol.BNBUSDT;
        Instant from = Instant.parse("2026-11-05T00:00:00Z");
        Instant to = Instant.parse("2026-11-05T01:00:00Z");

        List<PairPrice> fakePrices = List.of(
                new PairPrice(symbol, new BigDecimal("500.0"), from),
                new PairPrice(symbol, new BigDecimal("510.0"), from.plusSeconds(1800)),
                new PairPrice(symbol, new BigDecimal("520.0"), to)
        );

        when(repository.findBySymbolAndFetchedAtBetweenOrderByFetchedAtAsc(symbol, from, to))
                .thenReturn(fakePrices);

        TrendResponseDto result = service.calculateTrend(symbol, from, to);

        assertEquals("UPWARD", result.trend());
    }
}
