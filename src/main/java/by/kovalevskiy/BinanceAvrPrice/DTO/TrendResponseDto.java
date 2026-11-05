package by.kovalevskiy.BinanceAvrPrice.DTO;

import java.time.Instant;

public record TrendResponseDto(String symbol,
                               Instant from,
                               Instant to,
                               String trend) {
}
