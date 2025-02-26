package space.yong.orkes;

import com.netflix.conductor.sdk.workflow.task.WorkerTask;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class HelloWorker {
    @WorkerTask(value = "simple", threadCount = 100)
    public String hello() {
        System.out.println("simple");
        return "hello";
    }

    @WorkerTask(value = "HTTP", threadCount = 100)
    public Map http() {
        System.out.println("http");
        return Map.of("response", Map.of("body", "hello"));
    }

    @WorkerTask(value = "workflow_state_change_event_1af44afc-3dbd-46f6-8655-71d9965b7639", threadCount = 100)
    public String workflow_state_change() {
        System.out.println("workflow_state_change");
        return "hello";
    }

    @WorkerTask(value = "task_state_change_event_4f384e05-c52a-4312-b552-c23ac9e0e237", threadCount = 100)
    public Map task_state_change() {
        System.out.println("task_state_change");
        return Map.of("response", Map.of("body", "hello"));
    }
}
