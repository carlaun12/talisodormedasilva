# Engineering Architecture Baseline

Task: `P03-A03-002`  
Owner: `A03 — Engineering Architecture and Technical Governance`  
Status: Current baseline  
Last reviewed: 2026-10-04

## 1. Purpose and current scope

This document describes the smallest engineering architecture justified by the currently approved product scope.

The current requirements are:

- one initial user: the repository owner;
- Android as the only approved delivery surface;
- one approved capability: a calendar-oriented financial view;
- the view presents financial items by date, including obligations, credit-card-related obligations, and gains/income;
- product data is local to the device;
- no required backend/server;
- no required authentication;
- no required cloud synchronization;
- Etar/LineageOS calendar is a UX/reference point only;
- future capabilities and additional delivery surfaces remain undecided.

The architecture must not expand beyond those requirements.

ADR 0004 narrows the technology deferrals in ADR 0003 only where these requirements now provide sufficient evidence.

## 2. Selected foundation

The active implementation foundation is:

- one native Android application;
- Kotlin;
- Jetpack Compose for Android;
- Gradle with the Android Gradle Plugin and Gradle Wrapper;
- one initial Gradle module, `:app`;
- Room over SQLite for structured local persistence.

The following are not part of the active foundation:

- Desktop/JVM;
- Kotlin Multiplatform;
- Compose Multiplatform;
- a shared multiplatform module;
- backend or server infrastructure;
- authentication;
- networking;
- cloud synchronization.

Exact tool and library versions remain implementation choices subject to compatibility validation. Architecture selects the technology families, not an unverified version matrix.

## 3. Architectural posture

1. **One deployable Android application.** ADR 0002's single-deployable posture maps to one Android app for the current scope.
2. **One physical module until another is justified.** Start with `:app`; use logical boundaries before multiplying Gradle modules.
3. **Dependency direction remains inward.** UI and storage are adapters around application-owned concepts and contracts.
4. **The calendar is presentation, not the domain.** Financial-date information is the underlying application concern; the calendar grid is one visualization of it.
5. **No speculative financial platform.** Do not introduce budgets, reports, investments, bank integrations, card import, ledgers, accounts, sync, or multi-user architecture without product authority.
6. **Local state is authoritative.** Persisted product data lives on the device; no network source of truth exists in the current architecture.
7. **Technology boundaries stay reversible.** Android-specific frameworks should not leak into the minimal application/domain contracts when an owned contract is practical.

## 4. Minimum topology

Use one Gradle module:

```
:app
```

Within that module, keep these logical boundaries. They are package/dependency boundaries, not mandated submodules:

### Presentation

Responsibilities:

- Compose UI;
- calendar/date presentation;
- screen state;
- user interaction events;
- Android resource and lifecycle concerns.

Presentation may depend on application/domain contracts. It must not query Room directly.

### Application/domain

Responsibilities:

- minimal financial-date concepts required by the approved view;
- use-case/application coordination where behavior exists;
- persistence contracts required by the current capability;
- transformations that are independent of Android UI and Room.

Do not create a broad financial-management model. If a rule or concept is not required for the approved financial calendar view, leave it undefined.

### Local data adapter

Responsibilities:

- Room database;
- DAOs and persistence entities;
- mapping between persisted records and application-owned types;
- migrations.

Room-specific types must remain in this boundary.

### Platform wiring

Responsibilities:

- Android application/activity entry point;
- construction of concrete dependencies;
- lifecycle integration required to connect presentation and data.

A DI framework is not required. Manual construction is acceptable while the dependency graph remains small.

## 5. Dependency direction

Allowed direction:

```
Compose presentation
        |
        v
application/domain contracts
        ^
        |
Room local-data adapter

Android/platform wiring -> composes both sides
```

Rules:

- Compose does not own financial business state;
- Room entities do not become UI models;
- application/domain code does not import Compose or Room;
- persistence access is expressed through application-owned contracts;
- no network abstraction is created until networking is an approved requirement;
- avoid circular dependencies.

## 6. Android application architecture

Use unidirectional data flow for the screen boundary:

1. local persisted data is read through the data adapter;
2. application/state-holder logic transforms it into immutable UI state;
3. Compose renders that state;
4. UI events flow back to the state holder/application boundary;
5. approved mutations, when they exist, update the local source of truth.

A screen-level Android `ViewModel` is appropriate when lifecycle-stable state ownership is needed, but the architecture does not require a separate ViewModel class for trivial state that does not justify one.

The Etar/LineageOS reference may inform calendar interaction and visual exploration. It does not authorize calendar-provider integration, event semantics, recurrence, reminders, invitations, synchronization, or other calendar-product behavior.

## 7. Local persistence

Room over SQLite is the selected persistence technology for current structured device-local product data.

Rationale:

- the capability concerns a collection of structured financial items;
- items are visualized by date, making indexed/range-oriented queries a natural requirement;
- persisted local data requires schema evolution and migration discipline;
- Room provides Android-native SQLite access with query verification and migration support.

Persistence constraints:

- define only fields required by approved product behavior;
- do not invent future account, ledger, budget, investment, reporting, import, or sync schemas;
- version migrations from the first durable schema;
- keep database and DAO representations private to the data adapter;
- no cloud identifiers or synchronization metadata without a sync requirement;
- do not store secrets because no authentication or backend exists in the current scope.

## 8. Build and dependency governance

Use Gradle with the Android Gradle Plugin and the Gradle Wrapper.

Implementation must:

- select mutually compatible stable Android, Kotlin, Compose, AGP, Gradle, Room, and SDK versions;
- pin versions rather than use dynamic dependency versions;
- keep the build Android-only;
- avoid the Kotlin Multiplatform, Kotlin/JVM Desktop, and Compose Multiplatform plugins;
- add dependencies only for a concrete current capability;
- avoid introducing networking, authentication, analytics, serialization, DI, financial-calculation, or synchronization libraries without a requirement.

## 9. Testing baseline

Testing should cover the boundaries that now exist:

- deterministic tests for application/domain transformations and date grouping where implemented;
- persistence tests for Room queries and migrations once a schema exists;
- Compose UI tests for the approved calendar-oriented view where behavior warrants them;
- an Android application smoke test/build validation.

No test should imply unapproved financial behavior.

## 10. Security and privacy scope

The current application has:

- one local user;
- no authentication;
- no backend;
- no network requirement;
- local financial information on the device.

Therefore the current security boundary is primarily device-local data handling and dependency integrity.

Do not add account, token, remote-secret, network transport, server authorization, or cloud-security architecture until those capabilities are approved.

If future requirements classify the financial information as needing additional at-rest protection beyond normal Android application sandboxing, that requires an explicit security decision.

## 11. Delivery and operability

The deliverable is an Android application artifact.

The implementation foundation must support:

- reproducible builds through the Gradle Wrapper;
- traceability from source revision to application build;
- automated build/test validation when CI is introduced;
- database migration testing after durable schema creation.

Server health checks, distributed tracing, network SLOs, queue monitoring, and service deployment concerns are not applicable to the current scope.

## 12. Current decision state

Accepted in `P02-A03-001` and still applicable:

- architecture decisions are recorded as ADRs;
- use one deployable before distributed topology;
- maintain explicit dependency boundaries;
- require evidence before adopting infrastructure or vendor dependencies.

Selected in `P03-A03-002` through ADR 0004:

- Android-only active delivery foundation;
- Kotlin;
- Jetpack Compose for Android;
- Gradle/Android Gradle Plugin;
- one initial `:app` module;
- Room/SQLite local persistence;
- unidirectional presentation state flow;
- removal of Desktop/JVM, KMP, Compose Multiplatform, and the multiplatform `:shared` module from the active foundation.

Still deferred:

- backend/server technology;
- networking framework;
- authentication;
- synchronization;
- cloud provider;
- analytics;
- observability vendor;
- DI framework;
- serialization framework;
- financial calculation libraries;
- any second delivery surface;
- broader product-domain modeling.

These deferrals are intentional and should remain unresolved until requirements exist.
