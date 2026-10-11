# Paddleocr4J

Paddleocr4J is a synchronous Java client for the PaddleOCR HTTP API described by `openapi.json`. It can check the service health, analyze images or PDFs, and restructure the pages returned by an analysis.

The library provides:

- a reusable, thread-safe HTTP client;
- Java DTOs implemented as records;
- fluent builders for every request;
- enums for values currently known by the API;
- raw-value overloads for forward compatibility with newer server versions;
- consistent handling of network, HTTP, and JSON errors.

Full guides are available in the bilingual wiki:

- [English documentation](wiki/English.md)
- [Documentation française](wiki/Francais.md)

## Requirements

- JDK 21 recommended;
- a PaddleOCR server compatible with `openapi.json`;
- the Gradle Wrapper included in this repository;
- Gson 2.13.2, already declared in [`build.gradle`](build.gradle).

## Build

Linux or macOS:

```bash
./gradlew clean build
```

Windows:

```powershell
.\gradlew.bat clean build
```

The JAR is generated at `build/libs/Paddleocr4J-1.0.0.jar`.

PaddleOCR4J is available from Maven Central.

```groovy
dependencies {
    implementation 'fr.mrqsdf:PaddleOCR4J:1.0.0'
}
```

Maven:

```xml
<dependency>
    <groupId>fr.mrqsdf</groupId>
    <artifactId>PaddleOCR4J</artifactId>
    <version>1.0.0</version>
</dependency>
```

For a Gradle multi-project build:

```groovy
dependencies {
    implementation project(':Paddleocr4J')
}
```

## Supported endpoints

| HTTP method | Endpoint | Java method | Result |
|---|---|---|---|
| `GET` | `/health` | `getHealth()` | `AIStudioNoResultResponse` |
| `POST` | `/layout-parsing` | `infer(InferRequest)` | `AIStudioResponse<InferResult>` |
| `POST` | `/restructure-pages` | `restructurePages(RestructurePagesRequest)` | `AIStudioResponse<RestructurePagesResult>` |

## Create a client

```java
import fr.mrqsdf.paddleocr.PaddleOCRClient;

PaddleOCRClient client = PaddleOCRClient.init("http://localhost:8000");
```

The URL must be an absolute HTTP or HTTPS URL. A path prefix is supported:

```java
PaddleOCRClient client = PaddleOCRClient.init(
        "https://example.com/services/paddleocr"
);
```

The client uses a 30-second connection timeout and a 5-minute timeout for each request. Normal HTTP redirects are followed automatically.

## Check server health

```java
import fr.mrqsdf.paddleocr.dto.response.AIStudioNoResultResponse;

AIStudioNoResultResponse health = client.getHealth();

System.out.println(health.logId());
System.out.println(health.errorCode());
System.out.println(health.errorMsg());
```

## Analyze a document

`file` is the only mandatory property. Its value must use a format accepted by the deployed PaddleOCR server, such as encoded data or a file reference supported by that server.

### Minimal request

```java
import fr.mrqsdf.paddleocr.dto.request.InferRequest;
import fr.mrqsdf.paddleocr.dto.response.AIStudioResponse;
import fr.mrqsdf.paddleocr.dto.response.InferResult;

InferRequest request = InferRequest.builder(filePayload).build();
AIStudioResponse<InferResult> response = client.infer(request);
```

Unset properties remain `null`. Gson omits them from JSON, allowing the server to apply its defaults.

### Configured request

```java
import fr.mrqsdf.paddleocr.dto.request.InferRequest;
import fr.mrqsdf.paddleocr.dto.request.InputFileType;
import fr.mrqsdf.paddleocr.dto.request.LayoutShapeMode;
import fr.mrqsdf.paddleocr.dto.request.OutputFormat;

InferRequest request = InferRequest.builder(filePayload)
        .fileType(InputFileType.PDF)
        .outputFormat(OutputFormat.DOCX)
        .useDocOrientationClassify(true)
        .useDocUnwarping(true)
        .useLayoutDetection(true)
        .useChartRecognition(true)
        .useSealRecognition(false)
        .useOcrForImageBlock(true)
        .layoutThreshold(0.7)
        .layoutNms(true)
        .layoutUnclipRatio(1.2, 1.3)
        .layoutMergeBboxesMode("large")
        .layoutShapeMode(LayoutShapeMode.AUTO)
        .promptLabel("OCR")
        .formatBlockContent(true)
        .repetitionPenalty(1.1)
        .temperature(0.2)
        .topP(0.9)
        .minPixels(256)
        .maxPixels(4_000_000)
        .maxNewTokens(4096)
        .mergeLayoutBlocks(true)
        .markdownIgnoreLabels("header", "footer")
        .vlmExtraArg("seed", 42)
        .prettifyMarkdown(true)
        .showFormulaNumber(true)
        .returnMarkdownImages(true)
        .restructurePages(false)
        .mergeTables(true)
        .relevelTitles(true)
        .visualize(false)
        .logId("document-42")
        .build();

AIStudioResponse<InferResult> response = client.infer(request);
```

The values above demonstrate the builder API. Actual support depends on the models and configuration of the PaddleOCR server.

## `InferRequestBuilder` reference

| JSON property | Builder methods | Type or value | OpenAPI default |
|---|---|---|---|
| `file` | `InferRequest.builder(file)` | required non-empty `String` | none |
| `fileType` | `fileType(InputFileType)`, `fileType(int)` | `PDF=0`, `IMAGE=1`, or raw value | server |
| `outputFormats` | `outputFormat(...)`, `outputFormats(...)` | `OutputFormat` or raw `String` | no extra format |
| `useDocOrientationClassify` | `useDocOrientationClassify(boolean)` | orientation classification | server |
| `useDocUnwarping` | `useDocUnwarping(boolean)` | document unwarping | server |
| `useLayoutDetection` | `useLayoutDetection(boolean)` | layout detection | server |
| `useChartRecognition` | `useChartRecognition(boolean)` | chart recognition | server |
| `useSealRecognition` | `useSealRecognition(boolean)` | seal recognition | server |
| `useOcrForImageBlock` | `useOcrForImageBlock(boolean)` | OCR inside image blocks | server |
| `layoutThreshold` | `layoutThreshold(double/Map/JsonElement)` | global, per-label, or raw JSON | server |
| `layoutNms` | `layoutNms(boolean)` | layout NMS | server |
| `layoutUnclipRatio` | `layoutUnclipRatio(double/double,double/Map/JsonElement)` | global, pair, per-label, or raw JSON | server |
| `layoutMergeBboxesMode` | `layoutMergeBboxesMode(String/Map/JsonElement)` | global, per-label, or raw JSON | server |
| `layoutShapeMode` | `layoutShapeMode(LayoutShapeMode/String)` | `RECT`, `QUAD`, `POLY`, `AUTO`, or raw value | `auto` |
| `promptLabel` | `promptLabel(String)` | model label or instruction | server |
| `formatBlockContent` | `formatBlockContent(boolean)` | block formatting | server |
| `repetitionPenalty` | `repetitionPenalty(double)` | model repetition penalty | server |
| `temperature` | `temperature(double)` | sampling temperature | server |
| `topP` | `topP(double)` | top-p threshold | server |
| `minPixels` | `minPixels(int)` | strictly positive integer | server |
| `maxPixels` | `maxPixels(int)` | strictly positive integer | server |
| `maxNewTokens` | `maxNewTokens(int)` | strictly positive integer | server |
| `mergeLayoutBlocks` | `mergeLayoutBlocks(boolean)` | merge layout blocks | server |
| `markdownIgnoreLabels` | `markdownIgnoreLabel(String)`, `markdownIgnoreLabels(String...)` | ignored labels | server |
| `vlmExtraArgs` | `vlmExtraArg(String,Object)`, `vlmExtraArgs(Map)` | free-form VLM arguments | server |
| `prettifyMarkdown` | `prettifyMarkdown(boolean)` | format generated Markdown | `true` |
| `showFormulaNumber` | `showFormulaNumber(boolean)` | retain formula numbers | `false` |
| `returnMarkdownImages` | `returnMarkdownImages(boolean)` | return Markdown images | `true` |
| `restructurePages` | `restructurePages(boolean)` | restructure during analysis | `false` |
| `mergeTables` | `mergeTables(boolean)` | merge compatible tables | `true` |
| `relevelTitles` | `relevelTitles(boolean)` | recompute title levels | `true` |
| `visualize` | `visualize(boolean)` | request visualization output | server |
| `logId` | `logId(String)` | correlation identifier | server |

### Complex layout configuration

```java
InferRequest request = InferRequest.builder(filePayload)
        .layoutThreshold(Map.of("text", 0.6, "table", 0.8))
        .layoutUnclipRatio(Map.of("text", 1.1, "table", 1.4))
        .layoutMergeBboxesMode(Map.of("text", "large", "table", "union"))
        .build();
```

Raw JSON is also supported:

```java
import com.google.gson.JsonParser;

InferRequest request = InferRequest.builder(filePayload)
        .layoutThreshold(JsonParser.parseString("{\"future_label\":0.75}"))
        .build();
```

### Forward-compatible values

```java
InferRequest request = InferRequest.builder(filePayload)
        .fileType(2)
        .outputFormat("future-format")
        .layoutShapeMode("future-shape")
        .build();
```

## Restructure pages

Each `Page` contains the required `prunedResult` returned by an analysis and optional Markdown images.

```java
import fr.mrqsdf.paddleocr.dto.request.Page;
import fr.mrqsdf.paddleocr.dto.request.RestructurePagesRequest;
import fr.mrqsdf.paddleocr.dto.request.OutputFormat;

Page page = Page.builder(prunedResult)
        .markdownImage("images/figure-1.png", base64Image)
        .build();

RestructurePagesRequest request = RestructurePagesRequest.builder()
        .page(page)
        .outputFormat(OutputFormat.DOCX)
        .mergeTables(true)
        .relevelTitles(true)
        .concatenatePages(true)
        .prettifyMarkdown(true)
        .showFormulaNumber(false)
        .returnMarkdownImages(true)
        .logId("restructure-42")
        .build();

var response = client.restructurePages(request);
```

Pages can be added with `page(Page)`, `pages(Page...)`, or `pages(Iterable<Page>)`. Output formats have both enum and raw-string overloads.

## Chain analysis and restructuring

```java
import fr.mrqsdf.paddleocr.dto.request.Page;
import fr.mrqsdf.paddleocr.dto.request.PageBuilder;
import fr.mrqsdf.paddleocr.dto.request.RestructurePagesRequest;
import fr.mrqsdf.paddleocr.dto.response.LayoutParsingResult;

import java.util.ArrayList;
import java.util.List;

var inferResult = client.infer(inferRequest).result();
List<Page> pages = new ArrayList<>();

for (LayoutParsingResult result : inferResult.layoutParsingResults()) {
    PageBuilder pageBuilder = Page.builder(result.prunedResult());
    if (result.markdown().images() != null) {
        pageBuilder.markdownImages(result.markdown().images());
    }
    pages.add(pageBuilder.build());
}

RestructurePagesRequest restructureRequest = RestructurePagesRequest.builder()
        .pages(pages)
        .concatenatePages(true)
        .mergeTables(true)
        .build();

var restructured = client.restructurePages(restructureRequest);
```

## Response models

`AIStudioResponse<T>` exposes `logId`, `result`, `errorCode`, `errorMsg`, and `isSuccess()`. A zero application error code indicates success.

`InferResult` contains:

- `layoutParsingResults`: one or more `LayoutParsingResult` values;
- `dataInfo`: input metadata as a `JsonObject`.

Each `LayoutParsingResult` contains `prunedResult`, `markdown`, optional `outputImages`, optional `inputImage`, and optional `exports`. `MarkdownData` exposes the generated text and its image map. `DocumentExports` currently supports a DOCX `ExportContent` containing `content` and `fileName`.

The OpenAPI document also defines `ImageInfo`, `PDFInfo`, `TIFFInfo`, and `PDFPageInfo`. `InferResult.dataInfo` remains a `JsonObject` to preserve fields introduced by future server versions.

## Error handling

All network methods declare `throws PaddleOCRException`.

```java
try {
    var response = client.infer(request);
    if (!response.isSuccess()) {
        System.err.println(response.errorMsg());
    }
} catch (PaddleOCRException exception) {
    System.err.println(exception.getMessage());
    if (exception.getStatusCode() != null) {
        System.err.println("HTTP " + exception.getStatusCode());
        System.err.println(exception.getResponseBody());
    }
}
```

`PaddleOCRException` covers invalid URLs, transport failures, interrupted calls, non-2xx statuses, serialization failures, and invalid or empty JSON responses. For HTTP failures, `getStatusCode()` and `getResponseBody()` expose the original response details.

Local request validation uses `IllegalArgumentException` or `NullPointerException`. The library validates required content, positive pixel/token limits, required page data, and non-empty names or formats added through builders.

## Thread safety

`PaddleOCRClient` is immutable. Its shared `HttpClient` and Gson instance are thread-safe, so one client can be used concurrently:

```java
try (var executor = java.util.concurrent.Executors.newFixedThreadPool(8)) {
    var first = executor.submit(() -> client.infer(firstRequest));
    var second = executor.submit(() -> client.infer(secondRequest));

    var firstResult = first.get();
    var secondResult = second.get();
}
```

Builders are mutable working objects. Create one builder per request and per thread. Built requests use defensive copies for their main collections and JSON values. If an HTTP call is interrupted, the library restores the thread interruption flag before throwing `PaddleOCRException`.

## Tests and Javadoc

```bash
./gradlew clean test javadoc
```

Windows:

```powershell
.\gradlew.bat clean test javadoc
```

- Test report: `build/reports/tests/test/index.html`
- Javadoc: `build/docs/javadoc/index.html`

## Project structure

```text
Paddleocr4J/
├── openapi.json
├── build.gradle
├── README.md
├── wiki/
│   ├── EN-*.md
│   └── FR-*.md
└── src/
    ├── main/java/fr/mrqsdf/paddleocr/
    │   ├── PaddleOCRClient.java
    │   ├── PaddleOCRException.java
    │   ├── HttpClientUtil.java
    │   └── dto/
    │       ├── model/
    │       ├── request/
    │       └── response/
    └── test/java/
```

## Updating the OpenAPI contract

When the server contract changes:

1. update or compare `openapi.json`;
2. verify paths and HTTP methods;
3. update DTOs and builders for structural changes;
4. add stable enum values while keeping raw overloads;
5. add serialization or integration tests;
6. run `./gradlew clean test javadoc`.

Raw overloads can handle many new enum values immediately. New JSON fields or structures still require a library update.
