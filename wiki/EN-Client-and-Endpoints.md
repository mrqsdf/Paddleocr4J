# Client and endpoints

## Client initialization

```java
PaddleOCRClient client = PaddleOCRClient.init("https://example.com/paddleocr");
```

The URL must be absolute and use HTTP or HTTPS. A path prefix is preserved, so the example calls `https://example.com/paddleocr/health` and the other relative endpoints.

The client follows normal redirects, uses a 30-second connection timeout, and limits each request to 5 minutes.

## Health

`GET /health` is exposed by `getHealth()`:

```java
AIStudioNoResultResponse health = client.getHealth();
System.out.printf("%s: %s%n", health.errorCode(), health.errorMsg());
```

## Document analysis

`POST /layout-parsing` is exposed by `infer(InferRequest)`:

```java
InferRequest request = InferRequest.builder(filePayload)
        .fileType(InputFileType.IMAGE)
        .layoutShapeMode(LayoutShapeMode.AUTO)
        .returnMarkdownImages(true)
        .build();

AIStudioResponse<InferResult> response = client.infer(request);
```

The operation returns page-level layout results plus input metadata.

## Page restructuring

`POST /restructure-pages` is exposed by `restructurePages(RestructurePagesRequest)`:

```java
Page page = Page.builder(layoutResult.prunedResult())
        .markdownImages(layoutResult.markdown().images())
        .build();

RestructurePagesRequest request = RestructurePagesRequest.builder()
        .page(page)
        .concatenatePages(true)
        .mergeTables(true)
        .build();

AIStudioResponse<RestructurePagesResult> response =
        client.restructurePages(request);
```

Call `markdownImages(...)` only when the response map is non-null.

## Complete analysis-to-restructuring flow

```java
List<Page> pages = new ArrayList<>();

for (LayoutParsingResult result : client.infer(inferRequest)
        .result().layoutParsingResults()) {
    PageBuilder builder = Page.builder(result.prunedResult());
    if (result.markdown().images() != null) {
        builder.markdownImages(result.markdown().images());
    }
    pages.add(builder.build());
}

var result = client.restructurePages(
        RestructurePagesRequest.builder()
                .pages(pages)
                .concatenatePages(true)
                .build()
);
```

All network methods are synchronous and throw `PaddleOCRException` when transport, HTTP status validation, or JSON conversion fails.

Next: [[Request builders|EN-Request-Builders]].

[[Français|FR-Client-and-Endpoints]]
