package by.kovalevskiy.BinanceAvrPrice.DTO;

import java.util.List;

public record PagedResponseDto<T>(List<T> items,
                                  int page,
                                  int size,
                                  long totalElements,
                                  boolean hasNext) {
}
