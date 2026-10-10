# Responses and errors

## Response envelope

Analysis and restructuring return `AIStudioResponse<T>`:

| Accessor | Meaning |
|---|---|
| `logId()` | server correlation identifier |
| `result()` | typed operation result |
| `errorCode()` | application error code |
| `errorMsg()` | application message |
| `isSuccess()` | `true` when `errorCode == 0` |

Health returns `AIStudioNoResultResponse`, which contains the same metadata without a result.

## Analysis models

`InferResult` contains:

- `layoutParsingResults()`: analyzed pages or images;
- `dataInfo()`: input metadata kept as a Gson `JsonObject` for forward compatibility.

Each `LayoutParsingResult` exposes:

- `prunedResult()`: structured JSON reusable by page restructuring;
- `markdown()`: `MarkdownData` with text and optional images;
- `outputImages()`: optional generated images;
- `inputImage()`: optional returned input image;
- `exports()`: optional `DocumentExports`.

`DocumentExports.docx()` returns an `ExportContent` with `content()` and `fileName()`. Restructuring returns a `RestructurePagesResult` containing another list of `LayoutParsingResult` values.

The model package also contains `ImageInfo`, `PDFInfo`, `TIFFInfo`, and `PDFPageInfo`, matching the OpenAPI metadata variants.

## HTTP and conversion failures

```java
try {
    AIStudioResponse<InferResult> response = client.infer(request);
    if (!response.isSuccess()) {
        System.err.println(response.errorMsg());
    }
} catch (PaddleOCRException exception) {
    System.err.println(exception.getMessage());

    Integer status = exception.getStatusCode();
    if (status != null) {
        System.err.println("HTTP " + status);
        System.err.println(exception.getResponseBody());
    }
}
```

`PaddleOCRException` is used for invalid client URLs, I/O failures, interruption, non-2xx HTTP responses, serialization failures, and invalid or empty JSON responses. `getStatusCode()` and `getResponseBody()` are populated for HTTP failures and can be `null` otherwise.

An interrupted call restores the thread interruption flag before throwing the exception.

## Local validation failures

Request construction can throw `IllegalArgumentException` or `NullPointerException` before any HTTP call. Common causes are:

- missing or blank `file`;
- non-positive `minPixels`, `maxPixels`, or `maxNewTokens`;
- missing `prunedResult`;
- a null page list;
- blank image names, labels, format names, or map keys passed to builders.

## Application errors versus HTTP errors

A successful HTTP status can still carry an application `errorCode`. Check `isSuccess()` after a successful method call. A non-2xx HTTP status never returns a response DTO; it throws `PaddleOCRException` and preserves the raw response body.

[[Français|FR-Responses-and-Errors]]
