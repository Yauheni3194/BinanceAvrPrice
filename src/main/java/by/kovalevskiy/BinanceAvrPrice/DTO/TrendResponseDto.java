package by.kovalevskiy.BinanceAvrPrice.DTO;

import by.kovalevskiy.BinanceAvrPrice.models.Symbol;

import java.time.Instant;

public record TrendResponseDto(Symbol symbol,
                               Instant from,
                               Instant to,
                               String trend) {
}
