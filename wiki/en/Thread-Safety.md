# Thread safety

## Sharing the client

`PaddleOCRClient` is immutable. It uses one shared Java `HttpClient` and one shared Gson instance, both safe for concurrent use. Keep a single client for one server URL and reuse it across worker threads.

```java
PaddleOCRClient client = PaddleOCRClient.init("http://localhost:8000");

try (var executor = Executors.newFixedThreadPool(8)) {
    Future<AIStudioResponse<InferResult>> first =
            executor.submit(() -> client.infer(firstRequest));
    Future<AIStudioResponse<InferResult>> second =
            executor.submit(() -> client.infer(secondRequest));

    AIStudioResponse<InferResult> firstResult = first.get();
    AIStudioResponse<InferResult> secondResult = second.get();
}
```

Each invocation creates its own HTTP request and response objects. No per-call state is stored in the client.

## Builder ownership

Builders are mutable. Do not mutate one builder from several threads. Create a builder within the task that owns it:

```java
executor.submit(() -> {
    InferRequest request = InferRequest.builder(payload)
            .fileType(InputFileType.IMAGE)
            .build();
    return client.infer(request);
});
```

Built requests copy their main lists, maps, and Gson elements. This prevents later builder changes from modifying an already built request. Values placed inside free-form `vlmExtraArgs` can themselves be mutable; do not mutate such nested values while a request is being serialized.

## Interruption and cancellation

The client uses blocking `HttpClient.send`. Interrupting a worker causes a `PaddleOCRException`, and the interruption flag is restored. The current API does not expose asynchronous methods or a `CompletableFuture`; run synchronous calls in the executor chosen by the application.

## Recommended lifecycle

1. Create one `PaddleOCRClient` per base URL.
2. Reuse it for the application lifetime.
3. Build a fresh request in each operation.
4. Treat returned Gson `JsonObject` values as operation-owned mutable data, or copy them before sharing and mutation.

[[Français|FR-Thread-Safety]]
