package by.kovalevskiy.BinanceAvrPrice.controllers;
import by.kovalevskiy.BinanceAvrPrice.DTO.AveragePriceResponseDto;
import by.kovalevskiy.BinanceAvrPrice.DTO.PagedResponseDto;
import by.kovalevskiy.BinanceAvrPrice.DTO.PriceResponseDto;
import by.kovalevskiy.BinanceAvrPrice.DTO.TrendResponseDto;
import by.kovalevskiy.BinanceAvrPrice.models.Symbol;
import by.kovalevskiy.BinanceAvrPrice.services.PairPriceService;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;

@RestController
@RequestMapping("/api/v1/prices")
public class PairPriceController {
    private final PairPriceService service;

    public PairPriceController(PairPriceService service) {
        this.service = service;
    }

    @GetMapping("/latest")
    public PriceResponseDto getLatestPrice(@RequestParam(required = false) Symbol symbol) {
        return service.getLatestPrice(symbol);
    }

    @GetMapping("/average")
    public AveragePriceResponseDto getAveragePrice(
            @RequestParam(required = false) Symbol symbol,
            @RequestParam Instant from,
            @RequestParam Instant to) {
        return service.getAveragePrice(symbol, from, to);
        //TODO обработать ошибку
    }

    @GetMapping
    public PagedResponseDto<PriceResponseDto> getPrices(
            @RequestParam(required = false) Symbol symbol,
            @RequestParam Instant from,
            @RequestParam Instant to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return service.getPricesWithPagination(symbol, from, to, page, size);
        //TODO обработать ошибку
    }

    @GetMapping("/trend")
    public TrendResponseDto getTrend(
            @RequestParam(required = false) Symbol symbol,
            @RequestParam Instant from,
            @RequestParam Instant to) {
        return service.calculateTrend(symbol, from, to);
        //TODO обработать ошибку
    }
}
