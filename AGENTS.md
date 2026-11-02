# Coding Assistance Style Preference

## Context for Future Coding Assistance

When helping me with Java/Spring code, prioritize the **simplest solution that directly matches the code I already have**.

In particular, if I am already using `CompletableFuture`, prefer the appropriate `CompletableFuture` method (`supplyAsync`, `runAsync`, etc.) rather than introducing Spring-specific alternatives such as `@Async`, `@EnableAsync`, configuration changes, proxies, or additional application infrastructure unless there is a clear reason my existing approach cannot work.

Please avoid expanding a straightforward implementation into a different architectural approach unless I explicitly ask for alternatives or an explanation of why the current approach is inadequate.

When I ask for a concrete code change:
1. Show the minimal change first.
2. Explain alternatives separately, only if relevant.
3. Preserve my existing approach and conventions where practical.

## Error Handling and HTTP Status Codes

**Favor meaningful 4xx responses for client-caused errors instead of swallowing exceptions or returning null.**

This is particularly important when handling asynchronous operations with `CompletableFuture`.

- If an operation fails because of invalid input, duplicate identifiers, conflicts, or another client-caused issue, translate that failure into an appropriate HTTP 4xx response (e.g. `400 Bad Request`, `409 Conflict`).
- Never silently suppress exceptions using `.exceptionally(ex -> null)` or equivalent patterns that convert failures into successful completion.
- Never make a failed operation appear successful merely to prevent a 500 response.
- Preserve error visibility and meaningful failure semantics, especially if code or agents may execute autonomously.
- If an exception cannot reasonably be mapped to a 4xx, preserve the error rather than disguising it as success.
- Prefer the smallest change that correctly propagates or translates the exception using the existing approach.

**Important:** If context or an agent runs autonomously, error silencing must never happen. Failures must remain observable and produce appropriate error responses.

## General Preference

Work with the approach I'm already using, make the smallest necessary change, and don't introduce framework complexity unless it's actually needed.

### Preferred Example

If I have a `void` database operation and want it to execute asynchronously, the preferred solution is:

```java
CompletableFuture.runAsync(() -> {
    repository.doSomething();
});
```

rather than immediately redirecting me toward Spring's `@Async` mechanism.