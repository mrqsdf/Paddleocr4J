# Builders de requêtes

## Principes

- Les builders sont mutables et destinés à une requête et un thread.
- `build()` crée un instantané avec des copies défensives des principales collections et valeurs JSON.
- Les champs nullables non configurés sont omis du JSON.
- Les valeurs connues ont des enums ; les surcharges brutes acceptent de futures valeurs du serveur.

## `InferRequestBuilder`

Créez-le avec la chaîne obligatoire du document :

```java
InferRequestBuilder builder = new InferRequestBuilder(filePayload);
// Forme équivalente et généralement préférable :
InferRequestBuilder builder = InferRequest.builder(filePayload);
```

### Entrée et sorties

| Usage | Méthodes |
|---|---|
| Type d'entrée | `fileType(InputFileType)`, `fileType(int)` |
| Un format de sortie | `outputFormat(OutputFormat)`, `outputFormat(String)` |
| Plusieurs formats | `outputFormats(OutputFormat...)`, `outputFormats(String...)` |

Les types connus sont `PDF` (`0`) et `IMAGE` (`1`). Le format supplémentaire connu est `DOCX`. Les surcharges numériques et textuelles brutes assurent la compatibilité avec de futures valeurs.

### Prétraitement et reconnaissance

| Propriété JSON | Méthode du builder |
|---|---|
| `useDocOrientationClassify` | `useDocOrientationClassify(boolean)` |
| `useDocUnwarping` | `useDocUnwarping(boolean)` |
| `useLayoutDetection` | `useLayoutDetection(boolean)` |
| `useChartRecognition` | `useChartRecognition(boolean)` |
| `useSealRecognition` | `useSealRecognition(boolean)` |
| `useOcrForImageBlock` | `useOcrForImageBlock(boolean)` |

### Configuration du layout

| Propriété JSON | Formes acceptées |
|---|---|
| `layoutThreshold` | `double`, `Map<String, ?>`, `JsonElement` |
| `layoutNms` | `boolean` |
| `layoutUnclipRatio` | un `double`, paire horizontal/vertical, `Map<String, ?>`, `JsonElement` |
| `layoutMergeBboxesMode` | `String`, `Map<String, ?>`, `JsonElement` |
| `layoutShapeMode` | `LayoutShapeMode` ou `String` brut |

`LayoutShapeMode` fournit `RECT`, `QUAD`, `POLY` et `AUTO`.

```java
InferRequest request = InferRequest.builder(filePayload)
        .layoutThreshold(Map.of("text", 0.6, "table", 0.8))
        .layoutUnclipRatio(1.2, 1.3)
        .layoutMergeBboxesMode(Map.of("text", "large"))
        .layoutShapeMode(LayoutShapeMode.POLY)
        .build();
```

Utilisez un `JsonElement` lorsqu'une version plus récente du serveur attend une structure absente des surcharges pratiques.

### Génération par le modèle

| Propriété JSON | Méthode du builder |
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

Les limites de pixels et de tokens doivent être strictement positives lorsqu'elles sont définies.

### Markdown et restructuration

| Propriété JSON | Méthode du builder | Défaut OpenAPI |
|---|---|---|
| `mergeLayoutBlocks` | `mergeLayoutBlocks(boolean)` | serveur |
| `markdownIgnoreLabels` | `markdownIgnoreLabel(String)`, `markdownIgnoreLabels(String...)` | serveur |
| `prettifyMarkdown` | `prettifyMarkdown(boolean)` | `true` |
| `showFormulaNumber` | `showFormulaNumber(boolean)` | `false` |
| `returnMarkdownImages` | `returnMarkdownImages(boolean)` | `true` |
| `restructurePages` | `restructurePages(boolean)` | `false` |
| `mergeTables` | `mergeTables(boolean)` | `true` |
| `relevelTitles` | `relevelTitles(boolean)` | `true` |
| `visualize` | `visualize(boolean)` | serveur |
| `logId` | `logId(String)` | serveur |

## `PageBuilder`

```java
Page page = Page.builder(prunedResult)
        .markdownImage("images/chart.png", encodedImage)
        .markdownImages(otherImages)
        .build();
```

`prunedResult` est obligatoire et copié. Les noms et contenus des images ne doivent pas être vides.

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

Les pages peuvent être ajoutées individuellement, en varargs ou avec un `Iterable`. Les formats acceptent des valeurs typées ou brutes. Les défauts serveur sont `mergeTables=true`, `relevelTitles=true`, `concatenatePages=false`, `prettifyMarkdown=true`, `showFormulaNumber=false` et `returnMarkdownImages=true`.

Suite : [Réponses et erreurs](Responses-and-Errors.md).

[English](../en/Request-Builders.md)
