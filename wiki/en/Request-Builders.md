# Request builders

## Design rules

- Builders are mutable and intended for one request and one thread.
- `build()` creates request snapshots with defensive copies of the main collections and JSON elements.
- Unset nullable fields are omitted from JSON.
- Known values have enums; raw overloads accept future server values.

## `InferRequestBuilder`

Create it with the required document string:

```java
InferRequestBuilder builder = new InferRequestBuilder(filePayload);
// Equivalent and usually preferred:
InferRequestBuilder builder = InferRequest.builder(filePayload);
```

### Input and output

| Purpose | Methods |
|---|---|
| Input type | `fileType(InputFileType)`, `fileType(int)` |
| One output format | `outputFormat(OutputFormat)`, `outputFormat(String)` |
| Several output formats | `outputFormats(OutputFormat...)`, `outputFormats(String...)` |

Known input types are `PDF` (`0`) and `IMAGE` (`1`). The known extra output format is `DOCX`. Raw numeric and string overloads are deliberately available for forward compatibility.

### Preprocessing and recognition

| JSON property | Builder method |
|---|---|
| `useDocOrientationClassify` | `useDocOrientationClassify(boolean)` |
| `useDocUnwarping` | `useDocUnwarping(boolean)` |
| `useLayoutDetection` | `useLayoutDetection(boolean)` |
| `useChartRecognition` | `useChartRecognition(boolean)` |
| `useSealRecognition` | `useSealRecognition(boolean)` |
| `useOcrForImageBlock` | `useOcrForImageBlock(boolean)` |

### Layout configuration

| JSON property | Supported forms |
|---|---|
| `layoutThreshold` | `double`, `Map<String, ?>`, `JsonElement` |
| `layoutNms` | `boolean` |
| `layoutUnclipRatio` | one `double`, horizontal/vertical pair, `Map<String, ?>`, `JsonElement` |
| `layoutMergeBboxesMode` | `String`, `Map<String, ?>`, `JsonElement` |
| `layoutShapeMode` | `LayoutShapeMode` or raw `String` |

`LayoutShapeMode` provides `RECT`, `QUAD`, `POLY`, and `AUTO`.

```java
InferRequest request = InferRequest.builder(filePayload)
        .layoutThreshold(Map.of("text", 0.6, "table", 0.8))
        .layoutUnclipRatio(1.2, 1.3)
        .layoutMergeBboxesMode(Map.of("text", "large"))
        .layoutShapeMode(LayoutShapeMode.POLY)
        .build();
```

Use a `JsonElement` when a newer server expects a structure not represented by a convenience overload.

### Model generation

| JSON property | Builder method |
|---|---|
| `promptLabel` | `promptLabel(String)` |
| `formatBlockContent` | `formatBlockContent(boolean)` |
| `repetitionPenalty` | `repetitionPenalty(double)` |
| `temperature` | `temperature(double)` |
| `topP` | `topP(double)` |
| `minPixels` | `minPixels(int)` |
| `maxPixels` | `maxPixels(int)` |
| `maxNewTokens` | `maxNewTokens(int)` |
| `vlmExtraArgs` | `vlmExtraArg(String,Object)`, `vlmExtraArgs(Map<String, ?>)` |

Pixel and token limits must be strictly positive when set.

### Markdown and restructuring

| JSON property | Builder method | OpenAPI default |
|---|---|---|
| `mergeLayoutBlocks` | `mergeLayoutBlocks(boolean)` | server |
| `markdownIgnoreLabels` | `markdownIgnoreLabel(String)`, `markdownIgnoreLabels(String...)` | server |
| `prettifyMarkdown` | `prettifyMarkdown(boolean)` | `true` |
| `showFormulaNumber` | `showFormulaNumber(boolean)` | `false` |
| `returnMarkdownImages` | `returnMarkdownImages(boolean)` | `true` |
| `restructurePages` | `restructurePages(boolean)` | `false` |
| `mergeTables` | `mergeTables(boolean)` | `true` |
| `relevelTitles` | `relevelTitles(boolean)` | `true` |
| `visualize` | `visualize(boolean)` | server |
| `logId` | `logId(String)` | server |

## `PageBuilder`

```java
Page page = Page.builder(prunedResult)
        .markdownImage("images/chart.png", encodedImage)
        .markdownImages(otherImages)
        .build();
```

`prunedResult` is required and copied. Image names and contents must not be blank.

## `RestructurePagesRequestBuilder`

```java
RestructurePagesRequest request = RestructurePagesRequest.builder()
        .pages(pages)
        .outputFormat(OutputFormat.DOCX)
        .mergeTables(true)
        .relevelTitles(true)
        .concatenatePages(true)
        .prettifyMarkdown(true)
        .showFormulaNumber(false)
        .returnMarkdownImages(true)
        .logId("job-42")
        .build();
```

Pages can be added individually, as varargs, or as an `Iterable`. Output formats accept typed and raw values. Server defaults are `mergeTables=true`, `relevelTitles=true`, `concatenatePages=false`, `prettifyMarkdown=true`, `showFormulaNumber=false`, and `returnMarkdownImages=true`.

Next: [Responses and errors](Responses-and-Errors.md).

[Français](../fr/Request-Builders.md)
