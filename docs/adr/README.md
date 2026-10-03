# Architecture Decision Records

Task: `P02-A03-001`

This directory contains Architecture Decision Records (ADRs) for governed technical decisions.

## Status values

Use one of:

- Proposed
- Accepted
- Superseded
- Deprecated
- Rejected

## Naming

Use:

`NNNN-short-kebab-case-title.md`

Numbers are sequential and never reused.

## Required structure

Each ADR must contain:

1. Title
2. Status
3. Date
4. Task/reference
5. Context
6. Decision drivers
7. Considered options
8. Decision
9. Consequences
10. Revisit triggers

Add migration, security, operational, or rollback sections when relevant.

## Governance rule

Create an ADR before implementing a decision that materially changes:

- deployment topology;
- language/runtime/framework standard;
- durable datastore technology;
- externally visible protocol;
- authentication/authorization architecture;
- queue or event infrastructure;
- project-wide cloud/platform dependency;
- cross-module data ownership;
- security or reliability posture.

Small, local, reversible implementation choices do not require ADRs.

## Immutability

Accepted ADRs should not be silently rewritten to reflect a later decision. When the architecture changes materially, add a new ADR that supersedes the prior record and link both directions.
