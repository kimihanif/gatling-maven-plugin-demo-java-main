package galileo;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

public class SearchSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http.baseUrl("http://localhost:8080")
            .acceptHeader("application/json")
            .connectionHeader("keep-alive")
            .contentTypeHeader("application/json")
            .maxConnectionsPerHost(100)
            .shareConnections();

    ScenarioBuilder search = scenario("Search")
            .exec(http("Search Request")
                    .post("/flights/integration/galileo/v1/search")
                    .body(ElFileBody("galileo_search.json"))
                    .check(status().is(200)));

    {
        setUp(
                search.injectOpen(constantUsersPerSec(10).during(Duration.ofMinutes(3)))).protocols(httpProtocol);
    }
}
