package by.kovalevskiy.BinanceAvrPrice;

import by.kovalevskiy.BinanceAvrPrice.repositories.PairPriceRepository;
import by.kovalevskiy.BinanceAvrPrice.scheduler.BinanceScheduler;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class BinanceSchedulerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine")
            .withDatabaseName("fakeBinanceAvrPrice_db")
            .withUsername("postgres")
            .withPassword("postgres");

    private static WireMockServer wireMockServer;

    @Value("${binance.api.base-url}")
    private String baseUrl;

    @Autowired
    private PairPriceRepository repository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        if (wireMockServer == null) {
            startWireMock();
        }
        registry.add("binance.api.base-url", () -> "http://localhost:" + wireMockServer.port());
    }

    static void startWireMock() {
        wireMockServer = new WireMockServer(0);
        wireMockServer.start();
    }

    @AfterAll
    static void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    void shouldFetchAndSavePriceFromBinance() throws Exception {
        wireMockServer.stubFor(get(urlPathEqualTo("/api/v3/avgPrice"))
                .withQueryParam("symbol", equalTo("BNBUSDT"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"mins\":5,\"price\":\"650.12345678\"}")));

        BinanceScheduler scheduler = new BinanceScheduler(repository, baseUrl);

        scheduler.PriceFromBinance();

        long count = repository.count();
        assertTrue(count > 0, "Запись должна быть сохранена в PostgreSQL через Testcontainers");
    }
}
