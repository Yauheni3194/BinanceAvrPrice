package by.kovalevskiy.BinanceAvrPrice.DTO;

import by.kovalevskiy.BinanceAvrPrice.models.Symbol;

import java.math.BigDecimal;
import java.time.Instant;

public record PriceResponseDto(Symbol symbol,
                               BigDecimal price,
                               Instant fetchedAt) {
}
