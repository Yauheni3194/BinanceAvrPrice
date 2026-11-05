package by.kovalevskiy.BinanceAvrPrice.models;

import java.math.BigDecimal;

public record BinanceResponse(int mins,
                              BigDecimal price) {
}
