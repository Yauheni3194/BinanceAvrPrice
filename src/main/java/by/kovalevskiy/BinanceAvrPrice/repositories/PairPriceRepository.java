package by.kovalevskiy.BinanceAvrPrice.repositories;

import by.kovalevskiy.BinanceAvrPrice.models.PairPrice;
import by.kovalevskiy.BinanceAvrPrice.models.Symbol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PairPriceRepository extends JpaRepository<PairPrice, Long> {
    Optional<PairPrice> findFirstBySymbolOrderByFetchedAtDesc(Symbol symbol);
    Page<PairPrice> findBySymbolAndFetchedAtBetween(Symbol symbol, Instant from, Instant to, Pageable pageable);
    List<PairPrice> findBySymbolAndFetchedAtBetweenOrderByFetchedAtAsc(Symbol symbol, Instant from, Instant to);

}
