package pe.wallet.application.health;

import java.time.Instant;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import pe.wallet.contract.model.Health;

@ApplicationScoped
public class HealthService {

    private final String startedAt = Instant.now().toString();
    private final String environment;

    public HealthService(@ConfigProperty(name = "wallet.environment") String environment) {
        this.environment = environment;
    }

    public Health status() {
        return new Health(Health.StatusEnum.OK, Health.StorageEnum.MEMORY)
                .environment(environment)
                .startedAt(startedAt)
                .service("wallet-backend")
                .openapi("/contracts/openapi.json");
    }
}