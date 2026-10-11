# Bien démarrer

## Prérequis

- JDK 21 recommandé ;
- un serveur HTTP PaddleOCR compatible avec `openapi.json` ;
- le Gradle Wrapper inclus dans le dépôt.

## Compiler la bibliothèque

Linux ou macOS :

```bash
./gradlew clean build
```

Windows :

```powershell
.\gradlew.bat clean build
```

Le JAR est produit dans `build/libs/Paddleocr4J-1.0.0.jar`.

## L'ajouter à un autre projet

La bibliothèque est disponible sur Maven Central. Ajoutez-la à un projet Gradle avec :

```groovy
dependencies {
    implementation 'fr.mrqsdf:PaddleOCR4J:1.0.0'
}
```

Pour Maven :

```xml
<dependency>
    <groupId>fr.mrqsdf</groupId>
    <artifactId>PaddleOCR4J</artifactId>
    <version>1.0.0</version>
</dependency>
```

Dans un build Gradle multi-projet :

```groovy
dependencies {
    implementation project(':Paddleocr4J')
}
```

## Premier appel

```java
import fr.mrqsdf.paddleocr.PaddleOCRClient;
import fr.mrqsdf.paddleocr.dto.request.InferRequest;

PaddleOCRClient client = PaddleOCRClient.init("http://localhost:8000");

InferRequest request = InferRequest.builder(filePayload).build();
var response = client.infer(request);

if (response.isSuccess()) {
    response.result().layoutParsingResults().forEach(result ->
            System.out.println(result.markdown().text())
    );
}
```

`filePayload` doit utiliser une représentation acceptée par votre déploiement PaddleOCR. Le contrat OpenAPI définit ce champ comme une chaîne ; le format exact d'encodage ou de référence dépend du serveur.

## Valeurs par défaut du serveur

Les propriétés des builders sont nullables. Une option non configurée est omise par Gson, ce qui permet au serveur d'appliquer sa propre valeur par défaut. Configurez explicitement une option lorsque le comportement doit rester stable entre plusieurs configurations serveur.

Suite : [[Client et endpoints|FR-Client-and-Endpoints]].

[[English|EN-Getting-Started]]
