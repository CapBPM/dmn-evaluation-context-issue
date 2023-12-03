import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.engine.DecisionService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.junit5.ProcessEngineExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DrdTest {

    public ProcessEngine processEngine = ProcessEngineConfiguration
            .createStandaloneInMemProcessEngineConfiguration()
            .setJdbcUrl("jdbc:h2:mem:camunda;DB_CLOSE_DELAY=1000")
            .buildProcessEngine();

    @RegisterExtension
    ProcessEngineExtension extension = ProcessEngineExtension
            .builder()
            .useProcessEngine(processEngine)
            .build();

    @Test
    @Deployment(resources = {"drd-example.dmn"})
    void contextsTest(){
        DecisionService decisionService = processEngine.getDecisionService();

        Map<String, Object> variables = new HashMap<>();
        variables.put("a", 100);
        DmnDecisionResult rc = decisionService.evaluateDecisionByKey("d1")
                .variables(variables)
                .evaluate();

        Map<String,Object> data = (Map<String, Object>) rc.getSingleResult().get("d");

        // 7.19 no assertion
        // 7.20 assertion
        assertEquals(100L, data.get("c"));
    }

}
