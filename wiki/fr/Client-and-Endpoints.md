# Client et endpoints

## Initialiser le client

```java
PaddleOCRClient client = PaddleOCRClient.init("https://example.com/paddleocr");
```

L'URL doit être absolue et utiliser HTTP ou HTTPS. Un préfixe de chemin est conservé : l'exemple appelle donc `https://example.com/paddleocr/health` et les autres endpoints relatifs.

Le client suit les redirections normales, utilise un délai maximal de connexion de 30 secondes et limite chaque requête à 5 minutes.

## Santé du service

`GET /health` est exposé par `getHealth()` :

```java
AIStudioNoResultResponse health = client.getHealth();
System.out.printf("%s: %s%n", health.errorCode(), health.errorMsg());
```

## Analyse d'un document

`POST /layout-parsing` est exposé par `infer(InferRequest)` :

```java
InferRequest request = InferRequest.builder(filePayload)
        .fileType(InputFileType.IMAGE)
        .layoutShapeMode(LayoutShapeMode.AUTO)
        .returnMarkdownImages(true)
        .build();

AIStudioResponse<InferResult> response = client.infer(request);
```

L'opération renvoie les résultats de chaque page et les métadonnées de l'entrée.

## Restructuration des pages

`POST /restructure-pages` est exposé par `restructurePages(RestructurePagesRequest)` :

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

Appelez `markdownImages(...)` uniquement lorsque la map de la réponse n'est pas nulle.

## Cycle complet analyse et restructuration

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

Toutes les méthodes réseau sont synchrones et lèvent `PaddleOCRException` si le transport, la validation du statut HTTP ou la conversion JSON échoue.

Suite : [[Builders de requêtes|FR-Request-Builders]].

[[English|EN-Client-and-Endpoints]]
