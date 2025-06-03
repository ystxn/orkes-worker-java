package space.yong.orkes;

import com.netflix.conductor.sdk.workflow.executor.task.TaskContext;
import com.netflix.conductor.sdk.workflow.task.WorkerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;

@Slf4j
@Service
public class Workers {
    public record PersonQuery(String query) {}
    public record Person(String name, long id) {}

    @WorkerTask("lookup-person")
    public Person lookupPerson(PersonQuery input) {
        log.info("Received lookup-person: {}", input);
        TaskContext.get().addLog("Doing some work to lookup person");
        return new Person("John", 12345);
    }

    public record SecretStructure(
        Map<String, Object> _masked,
        Map<String, Object> _secrets
    ) {}

    @WorkerTask("do-sensitive-work")
    public SecretStructure doSensitiveWork(SecretStructure input) {
        log.info("Received do-sensitive-work: {}", input);
        TaskContext.get().addLog("Doing some sensitive work");
        return new SecretStructure(
            Map.of("masked-key", "masked-value"),
            Map.of("secret-key", "secret-value")
        );
    }

    public record IntensiveResult(double result) {}
    @WorkerTask(value = "do-intensive-task", threadCount = 10, pollingInterval = 20)
    public IntensiveResult doIntensiveTask() {
        log.info("Received do-intensive-task");
        TaskContext.get().addLog("Doing some intensive work");
        return new IntensiveResult(Math.random());
    }
}
