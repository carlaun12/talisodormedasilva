# Agent Governance and Execution Protocol

Established by: `P02-A02-001`

## 1. Task identity

Every agent task must have a stable TASK-ID.

The TASK-ID must appear in:

- progress reports;
- material decisions;
- commit messages;
- pull-request title or body;
- handoff notes;
- unresolved ambiguity reports.

## 2. Scope is a hard boundary

An agent may modify only the files or path patterns explicitly assigned to the task.

Being able to edit another path does not grant permission to do so.

If a correct solution appears to require an out-of-scope change, the agent must document the dependency and leave that change to the responsible task or owner.

## 3. Branch discipline

Each task should operate on the declared branch from the declared base.

Before editing, the agent should verify:

- repository identity;
- base branch exists;
- task branch state;
- current relevant files;
- whether concurrent changes affect the task.

Agents must not merge their own work unless the task explicitly authorizes merging.

## 4. Evidence hierarchy

Use the following precedence when determining what is known:

1. explicit current user/task instruction;
2. repository documentation and approved decision records;
3. existing source code and tests as evidence of current behavior;
4. clearly labeled assumptions.

Lower levels must not silently override higher levels.

## 5. Facts, decisions, assumptions, and ambiguities

Agents should classify material statements:

- **FACT** — directly observable from repository state or explicit instruction.
- **DECISION** — explicitly selected behavior or policy.
- **ASSUMPTION** — provisional premise used to continue work.
- **OPEN** — unresolved ambiguity requiring an owner decision.

An assumption is never equivalent to a decision.

## 6. Change design

Changes should be:

- minimal for the assigned objective;
- internally coherent;
- reviewable;
- reversible;
- free of unrelated cleanup;
- documented where behavior or governance changes.

## 7. Commit protocol

Commits must:

- reference the TASK-ID;
- describe the actual change;
- contain only in-scope files;
- avoid mixing unrelated tasks.

Recommended format:

`<TASK-ID>: <imperative summary>`

## 8. Pull-request protocol

A task PR should state:

- TASK-ID;
- objective;
- allowed scope;
- files changed;
- decisions made;
- assumptions introduced;
- unresolved ambiguities;
- validation performed;
- explicit exclusions.

A PR should not claim product approval for unresolved requirements.

## 9. Validation protocol

Before handoff, the agent must verify:

1. all modified paths are authorized;
2. branch/base match the task;
3. no accidental files were changed;
4. documentation links and identifiers are internally consistent;
5. decisions are distinguishable from assumptions;
6. unresolved ambiguity is reported;
7. the diff matches the requested responsibility.

Additional tests or linters should be run when relevant and available.

## 10. Concurrent-agent safety

When multiple agents may operate on the repository:

- do not reuse another agent's branch unless instructed;
- do not force-push over unknown work;
- inspect changes before rebasing or merging;
- treat conflicts as information, not as permission to discard other work;
- keep ownership boundaries explicit.

## 11. Product-governance rule

Implementation convenience must not define product behavior.

When the product specification is incomplete, an agent may propose options or record assumptions, but must not present an inferred option as approved scope.

## 12. Handoff standard

A complete handoff reports:

- TASK-ID;
- branch;
- base;
- commit SHA(s);
- PR number/URL if created;
- changed paths;
- validation result;
- decisions;
- unresolved ambiguities;
- next owner/action when applicable.
