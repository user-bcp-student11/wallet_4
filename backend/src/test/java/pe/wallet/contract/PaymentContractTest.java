package pe.wallet.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pe.wallet.contract.model.Payment;

class PaymentContractTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldPreserveCloudStringFingerprint() throws Exception {
        assertFingerprintRoundTrip("\"cloud-fingerprint\"");
    }

    @Test
    void shouldPreserveLocalArrayFingerprint() throws Exception {
        assertFingerprintRoundTrip("[\"wallet-demo\", null, 100]");
    }

    private void assertFingerprintRoundTrip(String fingerprint) throws Exception {
        String json = """
                {"id":"pay-test","wallet_id":"wallet-demo","recipient":"contact-demo",
                 "amount":100,"status":"PENDING","scenario":"APPROVED","idem":"test-key",
                 "fingerprint":%s}
                """.formatted(fingerprint);

        Payment payment = mapper.readValue(json, Payment.class);

        assertEquals(mapper.readTree(fingerprint), mapper.valueToTree(payment.getFingerprint()));
        assertEquals(mapper.readTree(fingerprint), mapper.readTree(mapper.writeValueAsString(payment)).get("fingerprint"));
    }
}