package pe.wallet.infrastructure.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sun.net.httpserver.HttpServer;
import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;
import io.quarkus.test.junit.QuarkusTest;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import pe.wallet.contract.model.Health;

@QuarkusTest
class WalletMockClientTest {

    @Test
    void shouldDeserializeGeneratedModelAndSendLatencyHeader() throws Exception {
        AtomicReference<String> latency = new AtomicReference<>();
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/health", exchange -> {
            latency.set(exchange.getRequestHeaders().getFirst("X-Mock-Latency-Ms"));
            byte[] payload = "{\"status\":\"ok\",\"storage\":\"memory\"}".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, payload.length);
            try (var response = exchange.getResponseBody()) {
                response.write(payload);
            }
        });
        server.start();

        try (WalletMockClient client = QuarkusRestClientBuilder.newBuilder()
                .baseUri(URI.create("http://127.0.0.1:" + server.getAddress().getPort()))
                .connectTimeout(2, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(5, java.util.concurrent.TimeUnit.SECONDS)
                .build(WalletMockClient.class)) {
            Health health = client.getHealth().await().atMost(Duration.ofSeconds(5));

            assertEquals(Health.StatusEnum.OK, health.getStatus());
            assertEquals(Health.StorageEnum.MEMORY, health.getStorage());
            assertEquals("0", latency.get());
        } finally {
            server.stop(0);
        }
    }
}