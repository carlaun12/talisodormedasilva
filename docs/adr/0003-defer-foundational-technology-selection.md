# ADR 0003: Defer Foundational Technology Selection Until Requirements Exist

Status: Accepted  
Date: 2026-10-03  
Task: `P02-A03-001`

## Context

At the time of this ADR, the repository contains no documented product requirements, user journeys, traffic model, data model, security classification, SLOs, recovery objectives, deployment target, or operational constraints.

Selecting foundational technologies without those inputs would optimize for preference rather than evidence and could impose avoidable migration cost.

## Decision drivers

- avoid arbitrary technology lock-in;
- preserve reversibility;
- tie platform choices to measurable requirements;
- prevent speculative complexity;
- enable alternatives to be compared using the same decision criteria.

## Considered options

### 1. Select a complete stack immediately

This can accelerate scaffolding but would make the current choice primarily preference-driven because the repository lacks the constraints needed to evaluate the trade-offs.

### 2. Select only popular/default technologies

Popularity can reduce ecosystem risk but is not a substitute for requirements concerning data semantics, latency, deployment, security, cost, or team constraints.

### 3. Defer foundational choices and define selection gates

This delays implementation-specific scaffolding while preserving a rational decision process.

## Decision

Defer selection of the following until sufficient requirements exist:

- programming language and runtime;
- application framework;
- frontend architecture and framework;
- primary datastore type and product;
- queue or message broker;
- cache;
- cloud/hosting provider;
- container/orchestration platform;
- CI/CD vendor;
- identity/authentication provider;
- observability vendor.

A technology may be selected when its ADR can state:

1. the concrete requirement being solved;
2. relevant quality attributes and target values where applicable;
3. at least one credible alternative;
4. operational and security consequences;
5. migration or replacement cost;
6. why the selected option is preferable under the known constraints.

Exploratory prototypes are allowed, but a prototype must not silently become the production standard without an ADR.

## Consequences

Positive:

- avoids false certainty;
- reduces premature lock-in;
- produces traceable selection rationale;
- enables technology decisions to reflect actual scale and risk.

Trade-offs:

- production scaffolding is intentionally delayed;
- teams cannot rely on an assumed stack until requirements are established;
- proof-of-concept work may need replacement after formal selection.

## Revisit triggers

This ADR should be revisited as soon as product scope and operational requirements are sufficiently defined to evaluate specific technologies.
