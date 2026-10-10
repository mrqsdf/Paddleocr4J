# Développement

## Organisation du dépôt

```text
Paddleocr4J/
├── openapi.json
├── build.gradle
├── README.md
├── wiki/en et wiki/fr
└── src/
    ├── main/java/fr/mrqsdf/paddleocr/
    │   ├── PaddleOCRClient.java
    │   ├── PaddleOCRException.java
    │   ├── HttpClientUtil.java
    │   └── dto/{model,request,response}
    └── test/java/
```

`PaddleOCRClient` est la façade publique de transport. `HttpClientUtil` gère HTTP et JSON. Les DTO de requête, builders et enums sont dans `dto.request`, les résultats dans `dto.response` et les métadonnées de document dans `dto.model`.

## Exécuter les contrôles

```bash
./gradlew clean test javadoc
```

Windows :

```powershell
.\gradlew.bat clean test javadoc
```

Résultats :

- rapport de tests : `build/reports/tests/test/index.html` ;
- Javadoc : `build/docs/javadoc/index.html` ;
- JAR : `build/libs/Paddleocr4J-1.0-SNAPSHOT.jar`.

Les tests exercent tous les endpoints avec un serveur HTTP local, la désérialisation des réponses génériques, les valeurs typées et brutes des builders, les détails des erreurs HTTP et l'utilisation concurrente d'un client.

## Mettre à jour le contrat OpenAPI

1. Remplacez ou comparez `openapi.json` avec le contrat serveur.
2. Examinez les chemins, méthodes, champs, valeurs par défaut et schémas modifiés.
3. Mettez à jour les DTO et builders pour les changements structurels.
4. Ajoutez aux enums les nouvelles valeurs stables documentées.
5. Conservez les surcharges brutes tant que le serveur les accepte.
6. Ajoutez des tests ciblés de sérialisation ou d'intégration HTTP.
7. Mettez à jour le README anglais et les deux langues du wiki.
8. Exécutez tous les contrôles.

Les surcharges brutes permettent souvent d'utiliser une nouvelle valeur d'enum avant une nouvelle version de la bibliothèque. Un nouveau champ ou une structure JSON différente nécessite une modification du code.

## Publier ce wiki sur GitHub

Le dossier `wiki` est prêt à être copié dans le dépôt Git séparé du Wiki GitHub. `Home.md` sert de sélecteur de langue et `_Sidebar.md` fournit la navigation. Conservez les dossiers `en` et `fr` lorsque votre méthode de publication accepte les fichiers imbriqués. Si un outil aplatit les noms de pages, conservez les préfixes de langue dans les noms de destination pour éviter les collisions.

[[English|EN-Development]]
