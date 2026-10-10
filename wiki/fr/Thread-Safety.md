# Sûreté en concurrence

## Partager le client

`PaddleOCRClient` est immuable. Il utilise un `HttpClient` Java et une instance Gson partagés, tous deux adaptés à l'utilisation concurrente. Conservez un seul client par URL de serveur et réutilisez-le depuis les threads de travail.

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

Chaque invocation crée ses propres objets de requête et de réponse HTTP. Aucun état propre à un appel n'est conservé dans le client.

## Propriété des builders

Les builders sont mutables. Ne modifiez pas un même builder depuis plusieurs threads. Créez le builder dans la tâche qui en est propriétaire :

```java
executor.submit(() -> {
    InferRequest request = InferRequest.builder(payload)
            .fileType(InputFileType.IMAGE)
            .build();
    return client.infer(request);
});
```

Les requêtes construites copient leurs principales listes, maps et valeurs Gson. Une modification ultérieure du builder ne change donc pas la requête. Les valeurs placées dans `vlmExtraArgs` peuvent elles-mêmes être mutables ; ne les modifiez pas pendant la sérialisation d'une requête.

## Interruption et annulation

Le client utilise l'appel bloquant `HttpClient.send`. L'interruption d'un worker produit une `PaddleOCRException` et le drapeau d'interruption est restauré. L'API actuelle n'expose pas de méthode asynchrone ni de `CompletableFuture` ; exécutez les appels synchrones dans l'exécuteur choisi par l'application.

## Cycle de vie recommandé

1. Créez un `PaddleOCRClient` par URL de base.
2. Réutilisez-le pendant toute la vie de l'application.
3. Construisez une nouvelle requête pour chaque opération.
4. Considérez les `JsonObject` retournés comme des données mutables propres à l'opération, ou copiez-les avant partage et modification.

[English](../en/Thread-Safety.md)
