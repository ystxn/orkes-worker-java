package space.yong.orkes;

import com.netflix.conductor.sdk.workflow.task.WorkerTask;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class Worker {
    @WorkerTask(value = "get-ads", threadCount = 5, pollingInterval = 200)
    public String getAds() {
        return "hello";
    }
}
