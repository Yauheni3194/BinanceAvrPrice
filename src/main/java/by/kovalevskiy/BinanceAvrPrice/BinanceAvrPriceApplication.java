package by.kovalevskiy.BinanceAvrPrice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BinanceAvrPriceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BinanceAvrPriceApplication.class, args);
	}
}
