package space.yong.orkes;

import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowTask;
import java.util.List;
import java.util.Map;

public class Util {
    public static WorkflowDef getSubFlow(String workflowName) {
        WorkflowTask simple = new WorkflowTask();
        simple.setName("simple");
        simple.setTaskReferenceName("simple_ref");
        simple.setType("SIMPLE");

        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setName(workflowName);
        workflowDef.setTasks(List.of(simple));
        return workflowDef;
    }

    public static WorkflowDef getDynamicForkWorkflow(String workflowName) {
        WorkflowTask dynamicFork = new WorkflowTask();
        dynamicFork.setName("fork_join_dynamic");
        dynamicFork.setTaskReferenceName("fork_join_dynamic_ref");
        dynamicFork.setType("FORK_JOIN_DYNAMIC");
        dynamicFork.setInputParameters(Map.of(
            "forkTaskWorkflow", "sub-flow",
            "forkTaskInputs", List.of(Map.of(), Map.of(), Map.of(), Map.of())
        ));

        WorkflowTask dynamicJoin = new WorkflowTask();
        dynamicJoin.setName("join");
        dynamicJoin.setTaskReferenceName("join_ref");
        dynamicJoin.setType("JOIN");

        WorkflowTask inline = new WorkflowTask();
        inline.setName("inline");
        inline.setTaskReferenceName("inline_ref");
        inline.setType("INLINE");
        inline.setInputParameters(Map.of(
            "expression", "(() => [0, 1, 2, 3].map(i => $.data['_fork_join_dynamic_ref_' + i].result).reduce((a, b) => a + b, 0))();",
            "evaluatorType", "graaljs",
            "data", "${join_ref.output}"
        ));

        WorkflowDef workflowDef = new WorkflowDef();
        workflowDef.setName(workflowName);
        workflowDef.setTasks(List.of(dynamicFork, dynamicJoin, inline));
        return workflowDef;
    }

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
