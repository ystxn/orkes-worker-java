package space.yong.orkes;

import io.orkes.conductor.client.http.OrkesWorkflowClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.Duration;

@Slf4j
@SpringBootTest
@Testcontainers
public abstract class BaseTest {
    private final String ORKES_IMAGE = "orkesio/orkes-conductor-standalone:4.1.53";

    @Autowired
    public OrkesWorkflowClient workflowClient;

    @Container
    static GenericContainer<?> orkes = new GenericContainer<>(ORKES_IMAGE)
        .withExposedPorts(8080)
        .waitingFor(Wait.forHttp("/health")
            .forPort(8080)
            .forStatusCode(200)
            .withStartupTimeout(Duration.ofMinutes(3))
        )
        .withLogConsumer(new Slf4jLogConsumer(log));

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("conductor.server.url", () -> String.format("http://localhost:%d/api", orkes.getMappedPort(8080)));
    }
}
