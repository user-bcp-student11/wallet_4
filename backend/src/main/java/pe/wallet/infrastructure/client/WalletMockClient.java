package pe.wallet.infrastructure.client;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import pe.wallet.contract.model.Health;

@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = "wallet-mock")
@ClientHeaderParam(name = "X-Mock-Latency-Ms", value = "${wallet.mock.latency-ms}")
public interface WalletMockClient extends AutoCloseable {

    @GET
    Uni<Health> getHealth();
}