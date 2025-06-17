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

    @Test
    public void testDynamicForkWorkflow() {
        metadataClient.registerWorkflowDef(Util.getSubFlow("sub-flow"));

        WorkflowTestRequest subRequest = new WorkflowTestRequest();
        subRequest.setName("sub-flow");
        subRequest.setTaskRefToMockOutput(Map.of(
            "simple_ref", List.of(new TaskMock(COMPLETED, Map.of("result", 3.14159)))
        ));

        WorkflowTestRequest testRequest = new WorkflowTestRequest();
        testRequest.setName("dynamic-fork-flow");
        testRequest.setWorkflowDef(Util.getDynamicForkWorkflow("dynamic-fork-flow"));
        testRequest.setSubWorkflowTestRequest(Map.of(
            "_fork_join_dynamic_ref_0", subRequest,
            "_fork_join_dynamic_ref_1", subRequest,
            "_fork_join_dynamic_ref_2", subRequest,
            "_fork_join_dynamic_ref_3", subRequest
        ));
        Workflow workflow = workflowClient.testWorkflow(testRequest);
        assertEquals(12.56636, workflow.getOutput().get("result"));
    }
}
