package by.kovalevskiy.BinanceAvrPrice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pair_prices")
public class PairPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "symbol")
    @Enumerated(EnumType.STRING)
    private Symbol symbol;

    @Column(precision = 18, scale = 8)
    private BigDecimal price;

    private Instant fetchedAt;

    public PairPrice(Symbol symbol, BigDecimal price, Instant fetchedAt) {
        this.symbol = symbol;
        this.price = price;
        this.fetchedAt = fetchedAt;
    }
}
