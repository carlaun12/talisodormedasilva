# Quality Attributes

Task: `P02-A03-001`  
Status: Baseline pending product targets

## Purpose

This document defines how non-functional requirements must be expressed and evaluated. It intentionally avoids invented target numbers while the repository has no product requirements, traffic model, data classification, or production topology.

## 1. Availability

Availability targets must identify:

- measured user journey or API surface;
- measurement window;
- excluded maintenance, if any;
- dependency assumptions;
- acceptable degraded modes.

A single global availability percentage is insufficient when different capabilities have different criticality.

## 2. Latency

Latency requirements must specify:

- operation;
- percentile, not only average;
- payload or workload shape;
- concurrency assumptions;
- geographic scope;
- dependency inclusion/exclusion.

At minimum, production-critical synchronous paths should eventually define p50, p95, and p99 expectations where meaningful.

## 3. Throughput and capacity

Capacity planning must define:

- steady-state throughput;
- expected peak;
- burst duration;
- growth assumption;
- maximum payload/data sizes;
- bottleneck strategy.

Infrastructure must not be selected from speculative scale assumptions.

## 4. Durability and recovery

Durable state must eventually define:

- Recovery Point Objective (RPO);
- Recovery Time Objective (RTO);
- backup frequency;
- restore verification;
- retention;
- regional or provider failure assumptions where applicable.

Backups that are not restoration-tested are not considered a complete recovery mechanism.

## 5. Security

Security requirements should cover:

- identity assurance;
- authorization granularity;
- data sensitivity;
- encryption;
- secret lifecycle;
- auditability;
- abuse resistance;
- dependency integrity;
- vulnerability response.

Security controls must be proportional to the data and threat model, not copied mechanically from unrelated systems.

## 6. Privacy

If personal or sensitive data is introduced, define:

- purpose of collection;
- minimization;
- retention;
- deletion;
- export/portability where required;
- access logging;
- third-party processors;
- geographic constraints where applicable.

## 7. Maintainability

Maintainability is evaluated through observable engineering properties:

- module dependency clarity;
- test execution time and determinism;
- change blast radius;
- build reproducibility;
- local development complexity;
- migration complexity;
- dependency update effort.

Architecture should optimize for the expected change pattern, not only initial delivery speed.

## 8. Operability

Before production, operators must be able to determine:

- whether the system is healthy;
- which dependency is failing;
- which release is running;
- whether errors are increasing;
- whether capacity is exhausted;
- how to recover or rollback.

## 9. Cost efficiency

Cost evaluation should include:

- baseline fixed cost;
- marginal cost per workload unit;
- operational labor;
- data transfer;
- storage growth;
- vendor switching cost.

Lower infrastructure spend is not necessarily lower total cost if operational complexity increases.

## 10. Portability and lock-in

Vendor-specific services may be chosen when their value exceeds migration risk.

For each material lock-in decision, document:

- reason for adoption;
- replacement boundary;
- data export path;
- migration difficulty;
- business benefit that justifies the coupling.

## 11. Quality-attribute gate

Before selecting foundational technology, the project should have enough evidence to answer:

1. What is being built?
2. Who uses it?
3. What data is stored?
4. Which operations are critical?
5. What failure is unacceptable?
6. What scale is expected in the next realistic planning horizon?
7. What recovery guarantees are required?
8. What regulatory/security constraints exist?
9. What is the deployment and operating model?

Until these are answered, technology selection is provisional.
