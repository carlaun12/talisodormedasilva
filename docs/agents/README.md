# AI Agent Governance

Governance baseline: `P02-A02-001`  
Current product-decision update: `P03-A02-002`

This directory defines how AI agents operate on the repository and records task-specific governance/decision handoffs.

## Principles

Agents must optimize for traceability, bounded scope, reversible changes, and explicit uncertainty.

An agent is not authorized by capability alone. Authorization comes from the active task contract: TASK-ID, branch/base, allowed paths, requested deliverables, and explicit user instructions.

## Core rules

1. Every progress report, decision, commit, pull request, and unresolved ambiguity must reference the active TASK-ID.
2. Modify only files permitted by the task.
3. Do not infer product requirements from repository names, code structure, technical scaffolding, or likely implementation choices.
4. Separate facts, decisions, assumptions, and open questions.
5. Prefer a small reviewable change over broad opportunistic cleanup.
6. Do not overwrite concurrent work without checking branch state.
7. Validate the final diff against the task scope before handoff.
8. Record unresolved blockers instead of concealing them.
9. A technical implementation cannot grant itself product authority.

## Files

- `governance.md` — repository-level agent execution protocol.
- `A02-P02-A02-001.md` — initial Product Specification and Agent Governance baseline.
- `A02-P03-A02-002.md` — Product Owner decision record establishing the narrow initial Android calendar scope.
