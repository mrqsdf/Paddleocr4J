# Documentation Paddleocr4J

Paddleocr4J est un client Java synchrone et thread-safe pour un service HTTP PaddleOCR compatible avec le contrat `openapi.json` du dépôt.

Il prend en charge le cycle complet :

1. vérifier l'état du serveur ;
2. analyser une image, un PDF ou un autre document pris en charge par le serveur ;
3. lire le Markdown, les images, les données structurées et les exports ;
4. envoyer éventuellement les pages analysées à l'endpoint de restructuration.

## Sommaire

- [Bien démarrer](Getting-Started.md) : prérequis, compilation, dépendance et premier appel.
- [Client et endpoints](Client-and-Endpoints.md) : cycle de vie du client et trois opérations HTTP.
- [Builders de requêtes](Request-Builders.md) : toutes les options, enums et valeurs brutes.
- [Réponses et erreurs](Responses-and-Errors.md) : DTO de réponse, erreurs applicatives et exceptions.
- [Sûreté en concurrence](Thread-Safety.md) : utilisation concurrente et propriété des builders.
- [Développement](Development.md) : tests, Javadoc, structure et mises à jour OpenAPI.

Le [README](../../README.md) fournit une présentation compacte en anglais. Pour les signatures Java exactes, générez la Javadoc avec `./gradlew javadoc`.

[English](../en/Home.md)
