# ADR 0004: Select the Initial Android Technology Foundation

Status: Accepted  
Date: 2026-10-04  
Task: `P03-A03-002`  
Related: ADR 0003

## Context

ADR 0003 deferred foundational technology selection because the repository did not yet contain enough product requirements to justify a runtime, application framework, frontend framework, or datastore.

The Product Owner has now supplied the first requirements that materially constrain those choices:

- the initial user is the repository owner;
- Android is the only approved delivery surface;
- the only currently approved capability is a calendar-oriented financial view;
- the view presents financial items by date, including obligations, credit-card-related obligations, and gains/income;
- product data is local to the device;
- no backend/server is required;
- no authentication is required;
- no cloud synchronization is required;
- Etar/LineageOS calendar is a UX/reference point, not an implementation requirement or feature set;
- future product capabilities and additional delivery surfaces remain undecided.

PR #3 contains an older Kotlin Multiplatform, Compose Multiplatform, Android, and Desktop/JVM foundation. That PR is implementation evidence only and is not architectural authority.

## Relationship to ADR 0003

This ADR narrows ADR 0003 only where current requirements now provide enough evidence to make a governed decision.

The following previously deferred decisions are selected here:

- programming language/runtime for the approved Android application;
- Android UI framework;
- Android build system;
- initial module topology;
- local persistence technology for the structured device-local financial-date data.

ADR 0003 remains in force for unrelated deferred technologies, including networking, backend infrastructure, cloud, authentication, synchronization, analytics, observability vendors, queues, caches, and other capabilities not required by the approved scope.

## Decision drivers

- Android is the only approved delivery surface;
- the project is greenfield on `main`;
- the current capability is a stateful, data-driven Android UI;
- data is structured, date-oriented, and local to the device;
- there is no current cross-platform sharing requirement;
- there is no backend, authentication, or synchronization requirement;
- the foundation should minimize build, runtime, and architectural complexity;
- technology choices should be reversible and should not encode speculative future product behavior.

## Considered options

### Option A: Android-only native application with Kotlin, Jetpack Compose, Gradle/AGP, and Room

Use the Android platform directly, Kotlin as the implementation language, Jetpack Compose for UI, Gradle with the Android Gradle Plugin for builds, one Android application module, and Room over SQLite for structured local persistence.

Advantages:

- directly matches the only approved delivery surface;
- uses Android's Kotlin-first language path;
- Compose is the current recommended Android UI toolkit and fits a state-driven calendar view;
- Gradle/AGP is the standard Android application build path;
- Room is designed for structured local data and provides query verification and migration support over SQLite;
- avoids cross-platform abstractions and duplicate platform targets;
- keeps the deployment boundary to one Android application.

Costs:

- Android-specific UI and persistence adapters are not directly portable to another platform;
- adding another platform later may require extracting portable logic from the Android project;
- Room introduces a durable storage dependency and schema migration responsibility.

### Option B: Kotlin Multiplatform with a shared module and Android application

Keep or rebuild the shape explored in PR #3: a KMP shared module plus an Android application, with future platform sharing left available.

Advantages:

- can share compatible business logic with another Kotlin-supported platform if one is later approved;
- can reduce future extraction work if meaningful cross-platform logic eventually exists.

Costs under current requirements:

- no second delivery surface is approved;
- introduces KMP source-set and plugin configuration, compatibility constraints, and an additional abstraction boundary without a current consumer;
- encourages designing portable abstractions before the required domain is known;
- increases build and dependency-management complexity for no current product outcome;
- risks preserving PR #3 because of sunk effort rather than requirement evidence.

Conclusion: not justified for the current phase.

### Option C: Android-only Kotlin with the Android Views/XML UI toolkit

Use the same Android-only runtime and build path but implement UI with Views/XML instead of Compose.

Advantages:

- mature toolkit with broad platform compatibility;
- appropriate when extending a substantial existing Views codebase or integrating View-specific components.

Costs under current requirements:

- `main` contains no legacy Android UI that must be preserved;
- a greenfield state-driven calendar view gains no migration advantage from Views;
- requires more imperative UI state coordination than Compose for the same data-driven screen model.

Conclusion: credible but inferior to Compose for this greenfield scope.

### Option D: Flutter/Dart targeting Android

Use Flutter as the application/UI runtime while shipping only Android initially.

Advantages:

- mature declarative UI toolkit;
- potential future cross-platform reuse.

Costs under current requirements:

- introduces a second runtime/toolchain and Dart language without a cross-platform requirement;
- moves away from the native Android/Kotlin ecosystem without a product constraint that benefits from doing so;
- does not reduce current architectural complexity compared with the native Android option.

Conclusion: credible but not justified.

## Local persistence alternatives

The approved capability displays a collection of structured financial items by date and the Product Owner requires data to remain local to the device. This is sufficient to select a structured on-device persistence mechanism without defining a broader financial domain.

Considered alternatives:

1. **Room over SQLite** — supports structured records, date-range queries, compile-time SQL verification, and explicit migrations.
2. **Raw SQLite APIs** — provides the same storage engine with more manual query, mapping, and migration work.
3. **DataStore** — appropriate for preferences or smaller typed settings, but not the preferred primary store for a queryable collection of financial items.
4. **Flat files / JSON** — initially simple but weak for indexed date queries, schema evolution, and transactional updates.

Room is selected. The database schema is not selected by this ADR beyond what the first implementation requires. No accounts, ledgers, budgets, investments, reports, imported transactions, synchronization metadata, or speculative future entities are authorized.

## Decision

Adopt **Option A** for the approved MVP foundation.

Specifically:

1. **Delivery/runtime**
   - Build one native Android application.
   - Do not maintain Desktop/JVM as an active target.

2. **Language**
   - Use Kotlin for application implementation.
   - This is an Android-only Kotlin decision, not a Kotlin Multiplatform decision.

3. **UI**
   - Use Jetpack Compose for Android.
   - Use a state-driven, unidirectional data-flow presentation model.
   - The calendar is a presentation of underlying financial-date information, not the owning domain model.

4. **Build**
   - Use Gradle with the Android Gradle Plugin.
   - Use the Gradle Wrapper for reproducible project builds.
   - Exact Kotlin, AGP, Gradle, Compose, and SDK versions remain implementation-time compatibility choices and must use mutually supported stable versions.

5. **Module topology**
   - Start with one Gradle application module: `:app`.
   - Do not create a KMP `:shared` module.
   - Enforce logical boundaries inside the application module before adding physical Gradle modules.
   - Introduce additional Gradle modules only when a concrete boundary, build-performance issue, reuse requirement, or isolation requirement justifies their cost.

6. **Logical boundaries and dependency direction**
   - presentation/UI depends on application/domain contracts;
   - application/domain code does not depend on Compose, Room, or other adapter implementations;
   - the local data adapter implements application-owned persistence contracts;
   - Android/platform wiring composes the implementation at the application edge;
   - no networking layer is introduced.

7. **Persistence**
   - Use Room over SQLite as the local structured persistence adapter.
   - Treat the local database as the source of truth for persisted financial-date information.
   - Keep Room entities and DAOs inside the data adapter and avoid leaking them into UI/domain contracts.
   - Define only the minimal schema required by the approved financial calendar capability.

8. **Explicit exclusions**
   - no Desktop/JVM target;
   - no Kotlin Multiplatform plugin or source sets;
   - no Compose Multiplatform dependency;
   - no backend/server;
   - no networking framework;
   - no authentication;
   - no cloud synchronization;
   - no analytics platform;
   - no DI framework mandated by architecture;
   - no serialization framework mandated by architecture;
   - no speculative financial-management modules.

## Consequences

### Positive

- the foundation directly matches the approved Android-only product surface;
- build and source topology are smaller than the PR #3 KMP/Desktop shape;
- UI architecture is aligned with a data-driven calendar presentation;
- persistence supports structured date queries and controlled schema evolution;
- platform-specific technology is isolated at the edges, preserving a future extraction path if another surface is later approved;
- unnecessary network, identity, and distributed-system concerns remain absent.

### Trade-offs

- a future second platform would require a new architectural decision and may require extracting portable logic from the Android codebase;
- Room creates Android/SQLite-specific migration work;
- keeping logical layers in one Gradle module relies on code review and package boundaries rather than compile-time module isolation.

## Migration and replacement cost

### Kotlin / Android

Replacing Kotlin with Java or another runtime after meaningful implementation would be high cost because application and UI code would need substantial rewriting. The choice is justified because Android is the approved surface and Kotlin is the platform's Kotlin-first development path.

### Compose

Replacing Compose with Views is medium-to-high cost after screens accumulate. The risk is accepted because the project is greenfield and no legacy Views investment exists.

### Room

Replacing Room while retaining SQLite is moderate cost if repository contracts remain stable: DAO/entity and migration code would change while application/domain contracts can remain intact.

Replacing SQLite with a different storage model would be higher cost because persisted data and migrations would need transformation. The repository boundary is therefore mandatory.

### Gradle/AGP

Replacing the Android build toolchain would be high cost and offers no current benefit. It is accepted as the standard Android build path.

### KMP

Not adopting KMP now does not prohibit it later. If another delivery surface becomes an approved requirement and there is demonstrably shareable non-UI logic, create a new ADR comparing extraction to KMP against platform-specific implementations at that time.

## PR #3 disposition

PR #3 should **not be merged in its current form** after this ADR is accepted.

Its Android intent is now directionally valid, but the following parts conflict with this decision:

- `desktopApp` target;
- Desktop/JVM runtime;
- Kotlin Multiplatform plugin and source sets;
- Compose Multiplatform as a cross-platform framework;
- `:shared` module created solely for multiplatform sharing.

A01 should rebuild or reconcile the implementation foundation from updated `main` against this ADR rather than preserving those elements because they already exist in PR #3.

## External technical evidence

- Android Kotlin-first guidance: https://developer.android.com/kotlin/first
- Android UI architecture and Compose recommendation: https://developer.android.com/topic/architecture/ui-layer
- Compose unidirectional data-flow guidance: https://developer.android.com/develop/ui/compose/architecture
- Android Gradle build overview: https://developer.android.com/build/gradle-build-overview
- Room local database guidance: https://developer.android.com/training/data-storage/room

## Revisit triggers

Create a new ADR if any of the following becomes an approved requirement or demonstrated engineering constraint:

- a second delivery surface;
- meaningful code sharing across independently delivered platforms;
- a persistence requirement Room/SQLite cannot satisfy;
- externally sourced or synchronized data;
- backend/server functionality;
- authentication or multi-user behavior;
- a module boundary that needs compile-time isolation;
- measured build performance that justifies physical modularization;
- platform-specific UI requirements that Compose cannot satisfy acceptably.
