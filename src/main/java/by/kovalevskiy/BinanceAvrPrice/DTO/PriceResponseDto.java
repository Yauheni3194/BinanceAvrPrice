package by.kovalevskiy.BinanceAvrPrice.DTO;

import java.math.BigDecimal;
import java.time.Instant;

public record PriceResponseDto(String symbol,
                               BigDecimal price,
                               Instant fetchedAt) {
}
