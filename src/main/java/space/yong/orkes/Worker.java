package space.yong.orkes;

import com.netflix.conductor.sdk.workflow.task.WorkerTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Worker {
    @WorkerTask("simple")
    public String simple() {
        return "hello";
    }
}
