package space.yong.orkes;

import static com.netflix.conductor.common.metadata.tasks.TaskResult.Status.COMPLETED;
import static com.netflix.conductor.common.run.WorkflowTestRequest.TaskMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.netflix.conductor.common.run.Workflow;
import com.netflix.conductor.common.run.WorkflowTestRequest;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;

public class WorkflowTests extends BaseTest {
    @Test
    public void testWorkflow() {
        WorkflowTestRequest testRequest = new WorkflowTestRequest();
        testRequest.setName("parent-flow");
        testRequest.setWorkflowDef(Util.getWorkflowDef("parent-flow"));
        testRequest.setTaskRefToMockOutput(Map.of(
            "lookup-person-ref", List.of(new TaskMock(COMPLETED, Map.of("name", "June", "id", 543321))),
            "do-sensitive-work-ref", List.of(new TaskMock(COMPLETED, Map.of("_masked", Map.of("q", "q"), "_secrets", Map.of("z", "z")))),
            "do-intensive-task-ref", List.of(new TaskMock(COMPLETED, Map.of("result", 3.14159))
            )));
        Workflow workflow = workflowClient.testWorkflow(testRequest);
        assertEquals(3.14159, workflow.getOutput().get("result"));
    }
}
