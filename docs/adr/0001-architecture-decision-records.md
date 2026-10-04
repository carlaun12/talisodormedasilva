# ADR 0001: Use Architecture Decision Records

Status: Accepted  
Date: 2026-10-03  
Task: `P02-A03-001`

## Context

The repository is at an initial stage and has no established mechanism for recording durable technical decisions. Architectural choices made implicitly in code become difficult to distinguish from accidental implementation details and are hard to revisit rationally.

## Decision drivers

- preserve decision context;
- distinguish intentional architecture from incidental implementation;
- make trade-offs reviewable;
- reduce repeated debate;
- provide explicit supersession history;
- support technical governance as the system evolves.

## Considered options

### 1. No formal decision log

Architecture would be inferred from code, pull requests, and commit history.

This has low initial overhead but loses rationale and makes cross-cutting decisions hard to audit.

### 2. Maintain one mutable architecture document only

A central document is useful for the current state, but rewriting it loses the historical reason behind changes.

### 3. Use Architecture Decision Records

Maintain concise immutable records for governed decisions and a separate architecture baseline for the current state.

## Decision

Use ADRs in `docs/adr/**` for governed technical decisions.

Accepted ADRs are historical records. Material changes must be captured in a new ADR that supersedes the previous decision instead of silently rewriting history.

Local, reversible implementation decisions do not require ADRs.

## Consequences

Positive:

- decision rationale remains available;
- trade-offs are explicit;
- reviewers can detect architecture changes;
- future reversals can reference original assumptions.

Costs:

- governed decisions require documentation;
- ADR quality depends on keeping scope focused;
- obsolete decisions require explicit supersession.

## Revisit triggers

Revisit this process if ADR volume becomes high enough to create material friction or if another governance mechanism demonstrably provides equivalent traceability with lower cost.
