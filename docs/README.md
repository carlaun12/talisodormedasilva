# Documentation

Project: `talisodormedasilva`  
Phase: `P02 — Product Specification`

This directory is the canonical home for product specification and AI-agent governance documentation.

## Structure

- `product/` — product definition, requirements, assumptions, decisions, and readiness gates.
- `agents/` — agent operating rules, task boundaries, execution protocol, and handoff rules.

## Documentation authority

A statement is considered an approved product requirement only when it is explicitly recorded in the product specification or in a linked decision record. Repository names, branch names, task names, and implementation guesses are not product requirements.

Agent instructions are authoritative only within the task and path scope granted to that agent. When task instructions conflict with repository-level governance, the narrower explicit task constraint wins unless it would require modifying content outside the allowed scope.

## Current baseline

The documentation baseline was established by task `P02-A02-001`. At creation time, the repository contained no product specification beyond the project identity and phase metadata supplied to Agent A02.

Unknown product decisions are intentionally recorded as unresolved rather than inferred.

## Change policy

Documentation changes should:

1. reference the responsible TASK-ID;
2. stay within the paths authorized for that task;
3. separate facts, decisions, assumptions, and open questions;
4. avoid silently converting assumptions into requirements;
5. preserve traceability from requirement to decision and later implementation.
