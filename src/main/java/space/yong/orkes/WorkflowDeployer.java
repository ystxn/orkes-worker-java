package space.yong.orkes;

import com.netflix.conductor.common.metadata.workflow.WorkflowDef;
import com.netflix.conductor.common.metadata.workflow.WorkflowTask;
import io.orkes.conductor.client.http.OrkesMetadataClient;
import io.orkes.conductor.client.model.TagObject;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkflowDeployer {
    public final OrkesMetadataClient metadataClient;
    public static final String WORKFLOW_NAME = "worker-java-demo";

    @PostConstruct
    public void deployWorkflow() {
        log.info("Deploying example workflow");

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
        workflowDef.setName(WORKFLOW_NAME);
        workflowDef.setVersion(1);
        workflowDef.setTasks(tasks);
        workflowDef.setDescription("Example workflow for orkes-worker-java demo");

        metadataClient.registerWorkflowDef(workflowDef, true);
        TagObject tag = new TagObject();
        tag.setKey("example");
        tag.setValue("worker");
        metadataClient.addWorkflowTag(tag, WORKFLOW_NAME);
        log.info("Example workflow deployed successfully: {}", WORKFLOW_NAME);
    }
}
