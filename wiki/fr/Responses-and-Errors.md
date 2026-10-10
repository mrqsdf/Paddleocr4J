# Réponses et erreurs

## Enveloppe de réponse

L'analyse et la restructuration renvoient `AIStudioResponse<T>` :

| Accesseur | Signification |
|---|---|
| `logId()` | identifiant de corrélation du serveur |
| `result()` | résultat typé de l'opération |
| `errorCode()` | code d'erreur applicatif |
| `errorMsg()` | message applicatif |
| `isSuccess()` | `true` lorsque `errorCode == 0` |

La santé renvoie `AIStudioNoResultResponse`, qui contient les mêmes métadonnées sans résultat.

## Modèles d'analyse

`InferResult` contient :

- `layoutParsingResults()` : pages ou images analysées ;
- `dataInfo()` : métadonnées de l'entrée conservées dans un `JsonObject` Gson pour la compatibilité future.

Chaque `LayoutParsingResult` expose :

- `prunedResult()` : JSON structuré réutilisable par la restructuration ;
- `markdown()` : `MarkdownData` avec texte et images facultatives ;
- `outputImages()` : images générées facultatives ;
- `inputImage()` : image d'entrée éventuellement renvoyée ;
- `exports()` : `DocumentExports` facultatif.

`DocumentExports.docx()` renvoie un `ExportContent` avec `content()` et `fileName()`. La restructuration renvoie un `RestructurePagesResult` contenant une nouvelle liste de `LayoutParsingResult`.

Le package des modèles contient aussi `ImageInfo`, `PDFInfo`, `TIFFInfo` et `PDFPageInfo`, conformément aux variantes de métadonnées OpenAPI.

## Erreurs HTTP et de conversion

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

`PaddleOCRException` couvre une URL client invalide, une erreur d'entrée/sortie, une interruption, une réponse HTTP non-2xx, un échec de sérialisation et une réponse JSON invalide ou vide. `getStatusCode()` et `getResponseBody()` sont renseignés pour une erreur HTTP et peuvent être `null` dans les autres cas.

Un appel interrompu restaure le drapeau d'interruption avant de lever l'exception.

## Erreurs de validation locales

La construction d'une requête peut lever `IllegalArgumentException` ou `NullPointerException` avant tout appel HTTP. Les causes fréquentes sont :

- `file` absent ou vide ;
- `minPixels`, `maxPixels` ou `maxNewTokens` inférieur ou égal à zéro ;
- `prunedResult` absent ;
- liste de pages nulle ;
- nom d'image, label, format ou clé de map vide transmis à un builder.

## Erreur applicative ou erreur HTTP

Un statut HTTP réussi peut contenir un `errorCode` applicatif. Vérifiez `isSuccess()` après le retour de la méthode. Un statut HTTP non-2xx ne renvoie jamais de DTO : il lève `PaddleOCRException` et conserve le corps brut.

[[English|EN-Responses-and-Errors]]
