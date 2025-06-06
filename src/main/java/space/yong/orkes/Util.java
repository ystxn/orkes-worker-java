package space.yong.orkes;

import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowTask;
import java.util.List;
import java.util.Map;

public class Util {
    public static WorkflowDef getWorkflowDef(String workflowName) {
        WorkflowTask lookupPersonTask = new WorkflowTask();
        lookupPersonTask.setName("lookup-person");
        lookupPersonTask.setTaskReferenceName("lookup-person-ref");
        lookupPersonTask.setInputParameters(Map.of("query", "john"));

        WorkflowTask doSensitiveWorkTask = new WorkflowTask();
        doSensitiveWorkTask.setName("do-sensitive-work");
        doSensitiveWorkTask.setTaskReferenceName("do-sensitive-work-ref");
        doSensitiveWorkTask.setInputParameters(Map.of(
            "_masked", Map.of("a", "b"),
            "_secrets", Map.of("a", "b")
        ));

        WorkflowTask doIntensiveTask = new WorkflowTask();
        doIntensiveTask.setName("do-intensive-task");
        doIntensiveTask.setTaskReferenceName("do-intensive-task-ref");
        doIntensiveTask.setInputParameters(Map.of("_masked", "${do-sensitive-work-ref.output._masked}"));

        List<WorkflowTask> tasks = List.of(lookupPersonTask, doSensitiveWorkTask, doIntensiveTask);
        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setName(workflowName);
        workflowDef.setVersion(1);
        workflowDef.setTasks(tasks);
        workflowDef.setDescription("Example workflow for orkes-worker-java demo");

        return workflowDef;
    }
}
