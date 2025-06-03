package space.yong.orkes;

import static com.netflix.conductor.common.metadata.tasks.TaskResult.Status.COMPLETED;
import static com.netflix.conductor.common.run.WorkflowTestRequest.TaskMock;
import com.netflix.conductor.common.run.Workflow;
import com.netflix.conductor.common.run.WorkflowTestRequest;
import io.orkes.conductor.client.http.OrkesWorkflowClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class WorkflowTests {
    @Autowired
    private OrkesWorkflowClient workflowClient;

    @Test
    public void testWorkflow() {
        WorkflowTestRequest testRequest = new WorkflowTestRequest();
        testRequest.setName(WorkflowDeployer.WORKFLOW_NAME);
        testRequest.setVersion(1);

        testRequest.setTaskRefToMockOutput(Map.of(
            "lookup-person-ref", List.of(new TaskMock(COMPLETED, Map.of("name", "June", "id", 543321))),
            "do-sensitive-work-ref", List.of(new TaskMock(COMPLETED, Map.of("_masked", Map.of("q", "q"), "_secrets", Map.of("z", "z")))),
            "do-intensive-task-ref", List.of(new TaskMock(COMPLETED, Map.of("result", 3.14159))
        )));

        Workflow workflow = workflowClient.testWorkflow(testRequest);
        assert workflow.getOutput().get("result").equals(3.14159);
    }
}
