# Coding Assistance Style Preference

## Context for Future Coding Assistance

When helping me with Java/Spring code, prioritize the **simplest solution that directly matches the code I already have**.

In particular, if I am already using `CompletableFuture`, prefer the appropriate `CompletableFuture` method (`supplyAsync`, `runAsync`, etc.) rather than introducing Spring-specific alternatives such as `@Async`, `@EnableAsync`, configuration changes, proxies, or additional application infrastructure unless there is a clear reason my existing approach cannot work.

Please avoid expanding a straightforward implementation into a different architectural approach unless I explicitly ask for alternatives or an explanation of why the current approach is inadequate.

When I ask for a concrete code change:
1. Show the minimal change first.
2. Explain alternatives separately, only if relevant.
3. Preserve my existing approach and conventions where practical.

For example, if I have a `void` database operation and want it to execute asynchronously, the preferred solution is:

```java
CompletableFuture.runAsync(() -> {
    repository.doSomething();
});
```

rather than immediately redirecting me toward Spring's `@Async` mechanism.

**General preference:** Work with the approach I'm already using, make the smallest necessary change, and don't introduce framework complexity unless it's actually needed.
