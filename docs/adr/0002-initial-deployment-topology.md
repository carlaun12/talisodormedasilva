# ADR 0002: Default to a Single Deployable with Modular Boundaries

Status: Accepted  
Date: 2026-10-03  
Task: `P02-A03-001`

## Context

No product requirements, traffic model, organizational boundaries, independent scaling needs, availability targets, or regulatory isolation constraints currently exist in the repository.

Introducing multiple deployable services at this stage would add network failure modes, deployment coordination, distributed observability, versioned inter-service contracts, and data-consistency complexity without evidence that those costs solve a real requirement.

At the same time, allowing an unstructured monolith would create internal coupling that is difficult to reverse.

## Decision drivers

- minimize premature distributed-systems complexity;
- preserve clear business-capability boundaries;
- keep deployment and local development simple;
- preserve a credible path to future service extraction;
- avoid architecture driven by speculative scale.

## Considered options

### 1. Multiple services from the start

Provides strong deployment separation but creates immediate operational and consistency overhead without demonstrated need.

### 2. Unstructured single application

Provides low initial operational complexity but makes future change and extraction expensive because boundaries remain implicit.

### 3. Modular single deployable

Keeps operational topology simple while requiring explicit internal boundaries and dependency rules.

## Decision

The default initial topology is one deployable application with explicit internal modules.

A module is not automatically a future microservice. It is a cohesive boundary with controlled dependencies and owned data responsibilities.

Introducing a new independently deployable service requires a separate ADR that demonstrates at least one concrete driver such as:

- independent scaling requirement;
- fault-containment requirement;
- materially different availability requirement;
- independent release cadence;
- regulatory or data-isolation requirement;
- ownership/team boundary that justifies operational separation;
- workload characteristics that cannot be handled efficiently in the existing deployable.

Source-code modularity alone is not sufficient justification for network distribution.

## Consequences

Positive:

- lower initial operational complexity;
- simpler local development and testing;
- fewer network failure modes;
- easier transactional consistency;
- lower deployment coordination cost;
- internal boundaries still support future extraction.

Trade-offs:

- the initial deployable can become large if module boundaries are not enforced;
- independent scaling is unavailable until a justified extraction occurs;
- accidental shared persistence can create hidden coupling if governance is ignored.

## Revisit triggers

Revisit when measured requirements demonstrate the need for independent deployment, scaling, isolation, ownership, or failure containment.
