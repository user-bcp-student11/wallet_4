package pe.wallet.application.health;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;
import pe.wallet.contract.model.Health;

class HealthServiceTest {

    @Test
    void shouldReturnIndependentSnapshotsWithStableStartTime() {
        HealthService service = new HealthService("test");
        Health first = service.status();
        Health second = service.status();

        assertNotSame(first, second);
        assertEquals(first.getStartedAt(), second.getStartedAt());
        assertEquals(Health.StatusEnum.OK, second.getStatus());
        assertEquals(Health.StorageEnum.MEMORY, second.getStorage());
        assertEquals("test", second.getEnvironment());
    }
}