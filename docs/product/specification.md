# Product Specification

Product authority updated by: `P03-A02-002`  
Prior baseline: `P02-A02-001`  
Status: **PARTIALLY SPECIFIED — initial Android calendar capability authorized; broader product remains OPEN**

This document records only product decisions explicitly authorized by the Product Owner. It does not define a complete financial-management product.

## 1. Status vocabulary

- **DECIDED** — explicitly approved and safe to treat as current product authority.
- **OPEN** — not yet decided; implementation must not infer a requirement.
- **OUT OF SCOPE** — explicitly excluded from the first implementation phase, without implying permanent rejection.

## 2. Product identity

**DECIDED**

- Repository/project identifier: `talisodormedasilva`.

No additional product meaning is inferred from the identifier.

Traceability: prior baseline `P02-A02-001`; retained by `P03-A02-002`.

## 3. Product Owner decision register

### PD-001 — Initial user

**DECIDED**

The initial intended user is the repository owner himself.

The public visibility of the repository does not make a public, multi-user, commercial, or general-consumer audience an approved requirement.

Future expansion to other users or audiences is **OPEN**.

Source: Product Owner decision supplied in task `P03-A02-002`.

### PD-002 — Initial delivery surface

**DECIDED**

Android is the initial product delivery surface.

Desktop/JVM is not an approved product delivery surface for the first implementation phase. Other delivery surfaces remain **OPEN** for future decisions and are not permanently rejected.

Kotlin, Kotlin Multiplatform, Compose, Gradle, and other implementation technologies are not product decisions in this specification. They remain subject to engineering architecture and technical governance.

Source: Product Owner decision supplied in task `P03-A02-002`.

### PD-003 — Initial product capability

**DECIDED**

The only committed product capability is a calendar-oriented financial view.

The initial user should be able to use a calendar-oriented experience to visualize financial items associated with dates, including examples such as:

- amounts he owes or needs to pay;
- obligations related to his credit cards;
- income or gains.

The calendar capability is the only currently committed feature. The broader product direction remains **OPEN**.

This decision does not define detailed financial entities, calculations, workflows, or additional management capabilities.

Source: Product Owner decision supplied in task `P03-A02-002`.

### PD-004 — Calendar UX reference

**DECIDED**

Etar (`https://github.com/LineageOS/android_packages_apps_Etar`) is a non-normative UX/navigation reference for a calendar-style experience.

The reference does not approve Etar's complete feature set, architecture, dependencies, data model, or integrations. Calendar Provider integration, synchronization, contacts, widgets, ICS import/export, multiple views, CalDAV-related workflows, and other Etar capabilities are not requirements unless separately approved.

Source: Product Owner decision supplied in task `P03-A02-002`.

### PD-005 — Data locality

**DECIDED**

For the initial product:

- product data should remain local on the device;
- no remote server or backend is required;
- no cloud synchronization is approved;
- no authentication or account infrastructure is required.

This is an initial-scope decision, not a permanent architectural prohibition. Remote services, synchronization, accounts, and related infrastructure remain **OPEN** for future decisions.

Persistence technology, database choice, schema, and exact data lifecycle remain **OPEN** technical/product-detail decisions.

Source: Product Owner decision supplied in task `P03-A02-002`.

## 4. Initial user problem and primary outcome

### Initial need

**DECIDED — narrow scope only**

For the first capability, the repository owner wants to visualize dated financial items through a calendar-oriented Android experience.

No broader problem statement is approved beyond this need.

Traceability: `PD-001`, `PD-002`, `PD-003`; task `P03-A02-002`.

### Primary outcome for the first capability

**DECIDED**

The initial user can view financial items in relation to dates and can distinguish money expected to enter from financial obligations expected to leave.

Traceability: `PD-003`; task `P03-A02-002`.

### Broader value proposition

**OPEN**

The repository does not yet define the product as a complete financial manager, budgeting tool, accounting system, investment product, banking product, or other broader category.

## 5. Initial functional requirements

### FR-001 — Calendar-oriented Android experience

**DECIDED**

The first implementation shall provide an Android product experience centered on a calendar-oriented interface.

Acceptance: see `AC-001`.

Source: `PD-002`, `PD-003`; task `P03-A02-002`.

### FR-002 — Dated financial items

**DECIDED**

The calendar experience shall be able to represent financial items associated with dates.

This requirement intentionally does not prescribe the entity schema, fields, persistence mechanism, or editing workflow.

Acceptance: see `AC-002`.

Source: `PD-003`; task `P03-A02-002`.

### FR-003 — Financial direction

**DECIDED**

The calendar experience shall distinguish financial items representing money expected to enter from financial obligations expected to leave.

This requirement does not prescribe calculations, status systems, categories, color semantics, or visual encoding.

Acceptance: see `AC-003`.

Source: `PD-003`; task `P03-A02-002`.

### FR-004 — No remote dependency for the initial experience

**DECIDED**

The first implementation shall be usable without requiring a remote server or user account.

This requirement does not select a persistence technology or permanently forbid future remote capabilities.

Acceptance: see `AC-004`.

Source: `PD-005`; task `P03-A02-002`.

## 6. Product-level acceptance criteria

### AC-001

An Android application can present a calendar-oriented interface.

### AC-002

Financial items associated with dates can be represented within that calendar experience.

### AC-003

The experience can distinguish money expected to enter from financial obligations expected to leave.

### AC-004

The demonstrated experience works without requiring a remote server or user account.

These criteria are product-level boundaries. They do not prescribe framework, language, database, storage engine, build system, or software architecture.

Traceability for `AC-001` through `AC-004`: task `P03-A02-002`, decisions `PD-002`, `PD-003`, and `PD-005`.

## 7. Data and integrations

### Current data boundary

**DECIDED**

Data used by the initial product is local to the device. The initial scope does not require backend services, cloud synchronization, or authentication/account infrastructure.

Traceability: `PD-005`; task `P03-A02-002`.

### Data details

**OPEN**

The following remain undecided:

- exact financial-item entity model and fields;
- exact credit-card model;
- installment model;
- recurrence behavior;
- transaction or obligation status model;
- persistence technology;
- database or storage schema;
- retention/deletion behavior beyond the current local-only boundary;
- financial calculations;
- monthly summaries or derived aggregates;
- import/export behavior.

No implementation choice may be promoted to product authority for these items without a later decision.

## 8. UX and interaction model

### Calendar interaction

**DECIDED**

The first product experience is calendar-oriented.

Etar is a non-normative UX/navigation reference only.

Traceability: `PD-003`, `PD-004`; task `P03-A02-002`.

### UX details

**OPEN**

The following remain undecided:

- exact calendar view structure;
- navigation hierarchy;
- date-selection behavior;
- item-entry/editing workflow;
- layout details;
- visual styling;
- color semantics;
- accessibility targets beyond any platform baseline later adopted;
- which, if any, Etar interaction patterns should be reproduced.

## 9. Explicitly out of scope for the first implementation phase

**OUT OF SCOPE**

Unless separately approved by a later Product Owner decision, the first implementation phase does not include:

- Desktop/JVM as a product delivery surface;
- budgeting;
- categories;
- investment tracking;
- net-worth tracking;
- reports;
- goals;
- bank integrations;
- transaction imports;
- dashboards;
- recurring-payment features;
- notifications;
- automation;
- account synchronization;
- cloud synchronization;
- financial advice;
- backend services;
- authentication or account infrastructure.

These exclusions limit the first implementation phase only. They are not permanent product rejections.

Traceability: `PD-002`, `PD-003`, `PD-005`; task `P03-A02-002`.

## 10. Still-open product questions

**OPEN**

Everything not explicitly decided above remains unresolved. Material open areas include:

- broader future product direction;
- whether the product will ever target users other than the repository owner;
- future delivery surfaces beyond Android;
- detailed creation, editing, deletion, and lifecycle behavior for financial items;
- exact treatment of credit-card obligations and installments;
- recurrence;
- categories or other classification;
- financial calculations and summaries;
- reporting, dashboard, notification, automation, import, integration, and synchronization capabilities;
- future remote services;
- future authentication/account model;
- data-retention and backup expectations;
- security/privacy requirements beyond the current local-only/no-account boundary;
- success metrics beyond satisfying the initial acceptance criteria;
- release/distribution policy.

Open items must not be filled by inference from implementation scaffolding or from the Etar reference.

## 11. Relationship to technical scaffolding

PR #3 and any Kotlin/Multiplatform/Compose/Gradle scaffolding are technical evidence only.

Current product authority is:

- Android: **DECIDED** as the initial delivery surface;
- Desktop/JVM: not approved for the first product scope;
- Kotlin/Kotlin Multiplatform/Compose/Gradle: not product decisions.

Engineering architecture and technical governance remain responsible for deciding whether any technical scaffold is acceptable under the product constraints now recorded.

Traceability: task `P03-A02-002`.

## 12. Implementation authorization boundary

The repository now has sufficient product authority to evaluate an **Android-only technical foundation** for the narrow first capability defined by `FR-001` through `FR-004`.

This does not authorize:

- a complete product implementation beyond those requirements;
- Desktop/JVM product delivery;
- additional financial-management features;
- a specific language, framework, multiplatform strategy, persistence technology, or architecture.

Any broader implementation remains blocked on later Product Owner and/or architecture decisions, as applicable.
