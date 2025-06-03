# Orkes Java SDK + Spring Example

This example demonstrates how to use the Orkes Java SDK's Spring integration to:
1. Deploy a workflow
2. Run workers
3. Run tests

## Configuration
The [`application.yaml`](src/main/resources/application.yaml) configuration file contains the following:
```yaml
conductor:
  server.url: ${ORKES_URI}
  security.client:
    key-id: ${ORKES_KEY_ID}
    secret: ${ORKES_SECRET}
```
These configuration parameters are read by the Orkes Java SDK's Spring integration to autowire the necessary beans.

Either add the environment variables `ORKES_URI`, `ORKES_KEY_ID`, and `ORKES_SECRET` to your system, or replace them with the actual values in the `application.yaml` file.

Alternatively, add another `application-profile.yaml` and launch your application with the `-Dspring.profiles.active=profile` option.

## Workflow Deployment
- On startup, the class [`WorkflowDeployer`](src/main/java/space/yong/orkes/WorkflowDeployer.java) automatically deploys the sample workflow named `worker-java-demo`.
- This workflow comprises a linear flow with 3 tasks:
    - `lookup-person`
    - `do-sensitive-work`
    - `do-intentive-task`

## Worker
- The workers are defined in the [`Workers`](src/main/java/space/yong/orkes/Workers.java) class
- Each worker is annotated with `@WorkerTask` and specifies the task type it handles
- Inputs and outputs can either be Maps or POJOs
- The `@WorkerTask` annotation also accepts other parameters like `threadCount` and `pollingInterval` to optimise performance

## Tests
- The test is defined in the [`WorkflowTests`](src/test/java/space/yong/orkes/WorkflowTests.java) class
- You can either reference an existing workflow assumed to be deployed on the target cluster, or define your workflow within the test itself
- The SDK allows you to mock simple task outputs using the `WorkflowTestRequest.setTaskRefToMockOutput()` method
- The `OrkesWorkflowClient.testWorkflow()` method fires a test execution that uses the provided mocks
