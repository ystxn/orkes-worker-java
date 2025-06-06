package space.yong.orkes;

import io.orkes.conductor.client.http.OrkesMetadataClient;
import io.orkes.conductor.client.model.TagObject;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkflowDeployer {
    public final OrkesMetadataClient metadataClient;
    public static final String WORKFLOW_NAME = "worker-java-demo";

    @PostConstruct
    public void deployWorkflow() {
        log.info("Deploying example workflow");
        metadataClient.registerWorkflowDef(Util.getWorkflowDef(WORKFLOW_NAME), true);
        TagObject tag = new TagObject();
        tag.setKey("example");
        tag.setValue("worker");
        metadataClient.addWorkflowTag(tag, WORKFLOW_NAME);
        log.info("Example workflow deployed successfully: {}", WORKFLOW_NAME);
    }
}
