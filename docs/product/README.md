# Product Specification

Baseline established by: `P02-A02-001`  
Initial Android calendar authority: `P03-A02-002`  
Current product authority updated by: `P03-A02-003`

## Purpose

This directory defines approved product behavior and explicitly records what remains unresolved so implementation choices do not become accidental requirements.

## Current product authority

The current product scope remains intentionally narrow:

- initial user: the repository owner;
- delivery surface: Android;
- minimum supported Android version: Android 15 / API 35;
- required Android support-floor configuration: `minSdk = 35`;
- first capability: a calendar-oriented view of financial items associated with dates;
- the calendar experience must distinguish expected money entering from financial obligations leaving;
- product financial data remains only on the current local device;
- Android cloud backup is disabled for product financial data;
- device-to-device application data transfer is disabled for product financial data;
- cloud synchronization, backend/server persistence, and account-based remote restore are disabled;
- Etar is a non-normative calendar UX/navigation reference only.

Android versions below API 35 and the disabled backup/transfer/remote-data paths are **OUT OF SCOPE** for the current product. These decisions may be reconsidered only through a future Product Owner decision.

The product is not yet specified as a complete financial manager. Everything not explicitly approved remains **OPEN** or, where stated, **OUT OF SCOPE**.

## Files

- `specification.md` — normative product decisions, requirements, acceptance criteria, open questions, and current-scope exclusions.

## Status vocabulary

- **DECIDED** — explicitly approved and safe to treat as current product authority.
- **ASSUMPTION** — temporary working premise that requires confirmation; not equivalent to a decision.
- **OPEN** — unresolved and not safe to infer.
- **OUT OF SCOPE** — explicitly excluded from the current implementation scope without implying permanent rejection.

## Authority boundary

Implementation scaffolding and engineering ADRs do not create product authority.

Product decisions now constrain the technical implementation to Android API 35+ and current-device-only financial data without Android cloud backup or device transfer. Technology-family choices remain governed by engineering architecture and technical decision processes.
