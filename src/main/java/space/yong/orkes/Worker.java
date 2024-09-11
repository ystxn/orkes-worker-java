package space.yong.orkes;

import com.netflix.conductor.sdk.workflow.executor.task.TaskContext;
import com.netflix.conductor.sdk.workflow.task.WorkerTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class Worker {
    public record FxInput(String currencyPair, Double amount) {}

    public Map<String, Double> rates = Map.of(
        "USDSGD", 1.31,
        "AUDSGD", 0.87
    );

    @WorkerTask("calculate-fx")
    public Double calculateFx(FxInput input) throws Exception {
        log.info("Received: {} of {}", input.amount(), input.currencyPair());
        Double rate = rates.get(input.currencyPair());
        TaskContext.get().addLog("Rate is " + rate);
        if (rate == null) {
            var result = TaskContext.get().getTaskResult();
            result.setOutputData(Map.of("error", "Currency pair not supported"));
            throw new Exception("Currency pair not supported");
        }
        return rate * input.amount();
    }
}
