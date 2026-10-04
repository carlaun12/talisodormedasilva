# AI Agent Governance

Task baseline: `P02-A02-001`

This directory defines how AI agents operate on the repository.

## Principles

Agents must optimize for traceability, bounded scope, reversible changes, and explicit uncertainty.

An agent is not authorized by capability alone. Authorization comes from the active task contract: TASK-ID, branch/base, allowed paths, requested deliverables, and explicit user instructions.

## Core rules

1. Every progress report, decision, commit, pull request, and unresolved ambiguity must reference the active TASK-ID.
2. Modify only files permitted by the task.
3. Do not infer product requirements from repository names, code structure, or likely implementation choices.
4. Separate facts from assumptions.
5. Prefer a small reviewable change over broad opportunistic cleanup.
6. Do not overwrite concurrent work without checking branch state.
7. Validate the final diff against the task scope before handoff.
8. Record unresolved blockers instead of concealing them.

## Files

- `governance.md` — repository-level agent execution protocol.
- `A02-P02-A02-001.md` — task contract and handoff record for Agent A02.
