# Product Specification

Baseline established by: `P02-A02-001`  
Current product authority updated by: `P03-A02-002`

## Purpose

This directory defines approved product behavior and explicitly records what remains unresolved so implementation choices do not become accidental requirements.

## Current product authority

The first product scope is intentionally narrow:

- initial user: the repository owner;
- initial delivery surface: Android;
- first capability: a calendar-oriented view of financial items associated with dates;
- the calendar experience must distinguish expected money entering from financial obligations leaving;
- initial product data remains local to the device;
- the initial experience does not require a backend, cloud synchronization, authentication, or user account;
- Etar is a non-normative calendar UX/navigation reference only.

The product is not yet specified as a complete financial manager. Everything not explicitly approved remains **OPEN** or, where stated, **OUT OF SCOPE** for the first implementation phase.

## Files

- `specification.md` — normative product decisions, functional requirements, acceptance criteria, open questions, and first-scope exclusions.

## Status vocabulary

- **DECIDED** — explicitly approved and safe to treat as current product authority.
- **ASSUMPTION** — temporary working premise that requires confirmation; not equivalent to a decision.
- **OPEN** — unresolved and not safe to infer.
- **OUT OF SCOPE** — explicitly excluded from the current implementation scope without implying permanent rejection.

## Authority boundary

Implementation scaffolding is evidence of technical choices, not product authority.

Android has Product Owner authority under `P03-A02-002`. Desktop/JVM does not have product-delivery authority for the first implementation phase. Kotlin, Kotlin Multiplatform, Compose, Gradle, persistence technology, and other technical choices remain governed by engineering architecture and technical decision processes.
