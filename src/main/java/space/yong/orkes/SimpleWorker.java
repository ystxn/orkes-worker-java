package space.yong.orkes;

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;

public class SimpleWorker implements Worker {
    private final int pollingInterval;

    public SimpleWorker(int pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    @Override
    public String getTaskDefName() {
        return "simple";
    }

    @Override
    public TaskResult execute(Task task) {
        TaskResult taskResult = new TaskResult(task);
        taskResult.setStatus(TaskResult.Status.COMPLETED);
        taskResult.getOutputData().put("message", "Hello");
        return taskResult;
    }

    @Override
    public int getPollingInterval() {
        return pollingInterval;
    }
}
