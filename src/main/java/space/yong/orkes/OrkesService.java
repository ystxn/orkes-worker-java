package space.yong.orkes;

import com.netflix.conductor.client.automator.TaskRunnerConfigurer;
import com.netflix.conductor.client.http.TaskClient;
import io.orkes.conductor.client.ApiClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrkesService {
    @Value("${conductor.server.url}")
    private String serverUrl;

    @Value("${conductor.security.client.key-id}")
    private String keyId;

    @Value("${conductor.security.client.secret}")
    private String secret;

    @Value("${conductor.worker.threadCount}")
    private int threadCount;

    @Value("${conductor.worker.pollingInterval}")
    private int pollingInterval;

    @PostConstruct
    public void init() {
        var apiClient = new ApiClient(serverUrl, keyId, secret);
        TaskRunnerConfigurer runnerConfigurer = new TaskRunnerConfigurer
            .Builder(new TaskClient(apiClient), List.of(new SimpleWorker(pollingInterval)))
            .withThreadCount(threadCount)
            .build();
        runnerConfigurer.init();
    }
}
