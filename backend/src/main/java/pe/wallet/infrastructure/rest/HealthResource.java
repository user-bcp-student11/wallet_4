package pe.wallet.infrastructure.rest;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import pe.wallet.application.health.HealthService;
import pe.wallet.contract.api.HealthApi;

@Path("/health")
public class HealthResource implements HealthApi {

    private final HealthService healthService;

    public HealthResource(HealthService healthService) {
        this.healthService = healthService;
    }

    @Override
    public Uni<Response> getHealth() {
        return Uni.createFrom().item(() -> Response.ok(healthService.status()).build());
    }
}