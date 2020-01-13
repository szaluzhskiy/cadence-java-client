package com.uber.cadence.reporter;

import com.uber.m3.tally.Buckets;
import com.uber.m3.tally.Capabilities;
import com.uber.m3.tally.CapableOf;
import com.uber.m3.tally.StatsReporter;
import com.uber.m3.util.Duration;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CadenceClientStatsReporter implements StatsReporter {

  private static final Logger log = LoggerFactory.getLogger(CadenceClientStatsReporter.class);
  private final List<String> metrics;

  public CadenceClientStatsReporter() {
    this.metrics = Arrays.asList(
        "cadence-workflow-endtoend-latency",
        "cadence-decision-execution-latency",
        "cadence-decision-response-latency",
        "cadence-workflow-start",
        "cadence-workflow-completed",
        "cadence-workflow-canceled",
        "cadence-workflow-failed",
        "cadence-workflow-continue-as-new"
    );
  }

  public CadenceClientStatsReporter(List<String> metrics) {
    this.metrics = metrics;
  }

  @Override
  public Capabilities capabilities() {
    return CapableOf.REPORTING;
  }

  @Override
  public void flush() {
    // NOOP
  }

  @Override
  public void close() {
    // NOOP
  }

  @Override
  public void reportCounter(String name, Map<String, String> tags, long value) {
    log.trace("CounterImpl {}: {} {}", name, tags, value);
    if (shouldReport(name)) {
      Metrics.counter(name, getTags(tags)).increment(value);
    }
  }

  @Override
  public void reportGauge(String name, Map<String, String> tags, double value) {
    // NOOP
  }

  @Override
  public void reportTimer(String name, Map<String, String> tags, Duration interval) {
    log.trace("TimerImpl {}: {} {}", name, tags, interval.getSeconds());
    if (shouldReport(name)) {
      Metrics.timer(name, getTags(tags)).record(interval.getNanos(), TimeUnit.NANOSECONDS);
    }
  }

  @Override
  public void reportHistogramValueSamples(String name, Map<String, String> tags, Buckets buckets,
      double bucketLowerBound, double bucketUpperBound, long samples) {
    // NOOP
  }

  @Override
  public void reportHistogramDurationSamples(String name, Map<String, String> tags, Buckets buckets,
      Duration bucketLowerBound, Duration bucketUpperBound, long samples) {
    // NOOP
  }

  private boolean shouldReport(String name) {
    return metrics.stream()
        .anyMatch(name::equals);
  }

  private Iterable<Tag> getTags(Map<String, String> tags) {
    return tags.entrySet()
        .stream()
        .map(entry -> Tag.of(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

}
