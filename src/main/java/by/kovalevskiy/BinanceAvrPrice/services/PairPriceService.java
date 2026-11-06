package by.kovalevskiy.BinanceAvrPrice.services;

import by.kovalevskiy.BinanceAvrPrice.DTO.AveragePriceResponseDto;
import by.kovalevskiy.BinanceAvrPrice.DTO.PagedResponseDto;
import by.kovalevskiy.BinanceAvrPrice.DTO.PriceResponseDto;
import by.kovalevskiy.BinanceAvrPrice.DTO.TrendResponseDto;
import by.kovalevskiy.BinanceAvrPrice.models.PairPrice;
import by.kovalevskiy.BinanceAvrPrice.models.Symbol;
import by.kovalevskiy.BinanceAvrPrice.repositories.PairPriceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class PairPriceService {
    private final PairPriceRepository repository;

    public PairPriceService(PairPriceRepository repository) {
        this.repository = repository;
    }

    public PriceResponseDto getLatestPrice(Symbol symbol) {
        return repository.findFirstBySymbolOrderByFetchedAtDesc(symbol)
                .map(p -> new PriceResponseDto(p.getSymbol(), p.getPrice(), p.getFetchedAt()))
                .orElseThrow(() -> new IllegalArgumentException("Введите корректное значение(пример:'BNBUSDT')"));
    }

    public AveragePriceResponseDto getAveragePrice(Symbol symbol, Instant from, Instant to) {
        Symbol targetSymbol = (symbol != null) ? symbol : Symbol.BNBUSDT;
        List<PairPrice> prices = repository.findBySymbolAndFetchedAtBetweenOrderByFetchedAtAsc(targetSymbol, from, to);
        if (prices.isEmpty()) {
            return new AveragePriceResponseDto(BigDecimal.ZERO);
        }
        else {
            BigDecimal sum = prices.stream()
                .map(PairPrice::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal average = sum.divide(BigDecimal.valueOf(prices.size()), 4, BigDecimal.ROUND_HALF_UP);
            return new AveragePriceResponseDto(average);}
    }

    public PagedResponseDto<PriceResponseDto> getPricesWithPagination(Symbol symbol, Instant from, Instant to, int page, int size) {
        Symbol targetSymbol = (symbol != null) ? symbol : Symbol.BNBUSDT;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("fetchedAt").ascending());

        Page<PairPrice> pricePage = repository.findBySymbolAndFetchedAtBetween(targetSymbol, from, to, pageRequest);

        List<PriceResponseDto> items = pricePage.getContent().stream()
                .map(p -> new PriceResponseDto(p.getSymbol(), p.getPrice(), p.getFetchedAt()))
                .toList();

        return new PagedResponseDto<>(
                items,
                pricePage.getNumber(),
                pricePage.getSize(),
                pricePage.getTotalElements(),
                pricePage.hasNext()
        );
    }

    public TrendResponseDto calculateTrend(Symbol symbol, Instant from, Instant to) {
        Symbol targetSymbol = (symbol != null) ? symbol : Symbol.BNBUSDT;
        List<PairPrice> prices = repository.findBySymbolAndFetchedAtBetweenOrderByFetchedAtAsc(targetSymbol, from, to);

        if (prices.size() < 2) {
            return new TrendResponseDto(targetSymbol, from, to, "FLAT");
        }
        else {
            int n = prices.size();
            double sumX = 0;
            double sumY = 0;
            double sumXY = 0;
            double sumXX = 0;

            for (PairPrice p : prices) {
                double x = p.getFetchedAt().getEpochSecond();
                double y = p.getPrice().doubleValue();
                sumX += x;
                sumY += y;
                sumXY += x * y;
                sumXX += x * x;
            }

            double slope = (n * sumXY - sumX * sumY) / (n * sumXX - sumX * sumX);

            double threshold = 0.0001;

            String trend;
            if (slope > threshold) {
                trend = "UPWARD";
            } else if (slope < -threshold) {
                trend = "DOWNWARD";
            } else {
                trend = "FLAT";
            }

            return new TrendResponseDto(targetSymbol, from, to, trend);}
    }
}
