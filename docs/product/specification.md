# Product Specification

Product authority updated by: `P03-A02-003`  
Previous product decision update: `P03-A02-002`  
Prior baseline: `P02-A02-001`  
Status: **PARTIALLY SPECIFIED — initial Android calendar capability authorized; broader product remains OPEN**

This document records only product decisions explicitly authorized by the Product Owner. It does not define a complete financial-management product.

## 1. Status vocabulary

- **DECIDED** — explicitly approved and safe to treat as current product authority.
- **OPEN** — not yet decided; implementation must not infer a requirement.
- **OUT OF SCOPE** — explicitly excluded from the current implementation phase, without implying permanent rejection.

## 2. Product identity

**DECIDED**

- Repository/project identifier: `talisodormedasilva`.

No additional product meaning is inferred from the identifier.

Traceability: prior baseline `P02-A02-001`; retained by `P03-A02-002` and `P03-A02-003`.

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

Persistence technology, database choice, schema, and exact data lifecycle remain separate technical/product-detail decisions.

Source: Product Owner decision supplied in task `P03-A02-002`.

**Current clarification:** `PD-007` further narrows the current-device data boundary by explicitly disabling Android cloud backup, device-to-device application-data transfer, backend/server persistence, cloud synchronization, and account-based remote restore for product financial data.

### PD-006 — Android support floor

**DECIDED**

The minimum supported Android version for the current product is **Android 15 / API 35**.

Consequences for the current product:

- Android versions below API 35 are **OUT OF SCOPE**;
- supporting older Android versions is not currently a requirement;
- the technical implementation must use `minSdk = 35`;
- any earlier ambiguity suggesting API 23 or another lower compatibility floor is superseded by this Product Owner decision.

This is the current support floor, not a permanent lifetime commitment. A future Product Owner decision may change it.

Source: Product Owner decision supplied in task `P03-A02-003`.

### PD-007 — Current-device-only financial data

**DECIDED**

Product financial data must remain only on the current local device.

For the current product, all of the following are disabled and are not requirements:

- Android cloud backup;
- device-to-device application data transfer;
- cloud synchronization;
- backend/server persistence;
- account-based remote restore.

No remote copy, backup infrastructure, synchronization mechanism, account-backed restore path, or cross-device migration mechanism should be inferred from the product scope.

This is a current product decision, not necessarily a permanent lifetime prohibition. A future Product Owner decision may change the locality/backup/transfer policy.

Source: Product Owner decision supplied in task `P03-A02-003`.

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

## 5. Initial functional and support requirements

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

### FR-005 — Android 15 / API 35 support floor

**DECIDED**

The current product supports Android 15 / API 35 and newer. Android versions below API 35 are outside the current product support scope.

The Android implementation configuration must reflect this product support floor with `minSdk = 35`.

Acceptance: see `AC-005`.

Source: `PD-006`; task `P03-A02-003`.

### FR-006 — Current-device-only financial data boundary

**DECIDED**

Product financial data shall remain only on the current local device. The current product shall not rely on or enable Android cloud backup, device-to-device application-data transfer, cloud synchronization, backend/server persistence, or account-based remote restore for that data.

This requirement does not select the local persistence implementation.

Acceptance: see `AC-006`.

Source: `PD-007`; task `P03-A02-003`.

## 6. Product-level acceptance criteria

### AC-001

An Android application can present a calendar-oriented interface.

### AC-002

Financial items associated with dates can be represented within that calendar experience.

### AC-003

The experience can distinguish money expected to enter from financial obligations expected to leave.

### AC-004

The demonstrated experience works without requiring a remote server or user account.

### AC-005

The current product declares Android 15 / API 35 as its minimum supported Android version, with Android versions below API 35 outside the supported product scope.

### AC-006

Product financial data remains confined to the current local device: the product does not provide or depend on Android cloud backup, device-to-device application-data transfer, cloud synchronization, backend/server persistence, or account-based remote restore for that data.

These criteria are product-level boundaries. Except where the Product Owner explicitly selected `minSdk = 35` as the required Android support-floor configuration, they do not prescribe framework, language, database, storage engine, build system, or software architecture.

Traceability:
- `AC-001` through `AC-004`: task `P03-A02-002`, decisions `PD-002`, `PD-003`, and `PD-005`;
- `AC-005`: task `P03-A02-003`, decision `PD-006`;
- `AC-006`: task `P03-A02-003`, decision `PD-007`.

## 7. Data and integrations

### Current data boundary

**DECIDED**

Product financial data is current-device-only.

For the current product:

- local device storage is the only approved location for product financial data;
- Android cloud backup is disabled;
- device-to-device application data transfer is disabled;
- cloud synchronization is disabled;
- backend/server persistence is disabled;
- account-based remote restore is disabled.

This supersedes the prior OPEN status of backup/recovery expectations insofar as they concern remote backup, remote restore, or cross-device transfer for the current product.

Traceability: `PD-005`, `PD-007`; tasks `P03-A02-002` and `P03-A02-003`.

### Data details

**OPEN**

The following remain undecided:

- exact financial-item entity model and fields;
- exact credit-card model;
- installment model;
- recurrence behavior;
- transaction or obligation status model;
- local database or storage schema;
- retention/deletion behavior on the current device;
- financial calculations;
- monthly summaries or derived aggregates;
- import/export behavior other than the explicitly disabled remote backup/restore and device-transfer paths.

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

## 9. Explicitly out of scope for the current implementation phase

**OUT OF SCOPE**

Unless separately approved by a later Product Owner decision, the current implementation phase does not include:

- Android versions below API 35;
- Desktop/JVM as a product delivery surface;
- Android cloud backup for product financial data;
- device-to-device application data transfer for product financial data;
- cloud synchronization;
- backend/server persistence;
- account-based remote restore;
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
- financial advice;
- authentication or account infrastructure.

These exclusions limit the current implementation phase only. They are not permanent product rejections.

Traceability:
- `PD-002`, `PD-003`, `PD-005`; task `P03-A02-002`;
- `PD-006`, `PD-007`; task `P03-A02-003`.

## 10. Still-open product questions

**OPEN**

Everything not explicitly decided above remains unresolved. Material open areas include:

- broader future product direction;
- whether the product will ever target users other than the repository owner;
- future delivery surfaces beyond Android;
- whether a future Product Owner decision should support Android versions below API 35;
- detailed creation, editing, deletion, and lifecycle behavior for financial items;
- exact treatment of credit-card obligations and installments;
- recurrence;
- categories or other classification;
- financial calculations and summaries;
- reporting, dashboard, notification, automation, import, integration, and synchronization capabilities not currently approved;
- whether future remote services, backup, restore, transfer, or synchronization should ever be introduced;
- future authentication/account model;
- on-device retention and deletion expectations;
- security/privacy requirements beyond the current-device-only/no-account boundary;
- success metrics beyond satisfying the initial acceptance criteria;
- release/distribution policy.

Open items must not be filled by inference from implementation scaffolding or from the Etar reference.

## 11. Relationship to technical architecture

Current product authority is:

- Android: **DECIDED** as the initial delivery surface;
- Android 15 / API 35: **DECIDED** as the minimum supported Android version;
- `minSdk = 35`: required to reflect the selected support floor;
- Desktop/JVM: not approved for the current product scope;
- product financial data: current-device-only, with Android cloud backup and device transfer disabled;
- cloud synchronization, backend/server persistence, and account-based remote restore: disabled for the current product.

Technology-family decisions such as Kotlin, Jetpack Compose, Gradle/AGP, and Room are governed by engineering architecture and ADRs. Product decisions in `P03-A02-003` constrain those technical choices but do not otherwise redesign the architecture.

Traceability: tasks `P03-A02-002` and `P03-A02-003`.

## 12. Implementation authorization boundary

The repository has sufficient product authority to implement and evaluate the narrow Android calendar capability defined by `FR-001` through `FR-006`, subject to the current architecture authority.

This does not authorize:

- a complete product implementation beyond those requirements;
- Android support below API 35;
- Desktop/JVM product delivery;
- remote backup, cross-device transfer, cloud synchronization, backend persistence, or account-based remote restore of product financial data;
- additional financial-management features not explicitly approved.

Any broader implementation remains blocked on later Product Owner and/or architecture decisions, as applicable.
