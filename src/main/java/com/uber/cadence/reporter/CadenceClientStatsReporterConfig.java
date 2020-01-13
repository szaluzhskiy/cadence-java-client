package com.uber.cadence.reporter;

import java.util.Arrays;
import java.util.List;

public interface CadenceClientStatsReporterConfig {

  default List<String> getMetrics() {
    return Arrays.asList(
        "cadence-workflow-endtoend-latency",
        "cadence-decision-execution-latency",
        "cadence-decision-response-latency",
        "cadence-workflow-start",
        "cadence-workflow-completed",
        "cadence-workflow-canceled",
        "cadence-workflow-failed",
        "cadence-workflow-continue-as-new"
    );
  };

}
