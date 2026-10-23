package pe.wallet.infrastructure.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class HealthResourceTest {

    @Test
    void shouldExposeHealthUsingGeneratedContract() {
        given()
                .when().get("/health")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("status", is("ok"))
                .body("storage", is("memory"))
                .body("environment", not(emptyOrNullString()))
                .body("service", is("wallet-backend"))
                .body("startedAt", not(emptyOrNullString()))
                .body("openapi", is("/contracts/openapi.json"));
    }

    @Test
    void shouldExposeTechnicalReadiness() {
        given().when().get("/q/health/ready").then()
                .statusCode(200).body("status", is("UP"));
    }

    @Test
    void shouldNotPretendPaymentBusinessIsImplemented() {
        given().when().get("/payments").then().statusCode(404);
    }

    @Test
    void shouldPublishTheOriginalContract() {
        given().when().get("/contracts/openapi.json").then()
                .statusCode(200)
                .body("openapi", is("3.0.3"))
                .body("info.version", is("0.2.0"));
    }
}