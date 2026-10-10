# Getting started

## Requirements

- JDK 21 recommended;
- a PaddleOCR HTTP server compatible with `openapi.json`;
- the Gradle Wrapper included in the repository.

## Build the library

Linux or macOS:

```bash
./gradlew clean build
```

Windows:

```powershell
.\gradlew.bat clean build
```

The JAR is written to `build/libs/Paddleocr4J-1.0-SNAPSHOT.jar`.

## Add it to another project

The library is not currently published to Maven Central. Copy the JAR into the consuming project's `libs` directory and declare both the JAR and Gson:

```groovy
dependencies {
    implementation files('libs/Paddleocr4J-1.0-SNAPSHOT.jar')
    implementation 'com.google.code.gson:gson:2.13.2'
}
```

For a Gradle multi-project build:

```groovy
dependencies {
    implementation project(':Paddleocr4J')
}
```

## First request

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

`filePayload` must use a representation accepted by your PaddleOCR deployment. The OpenAPI contract defines it as a string; the exact accepted encoding or reference format belongs to the server.

## Server defaults

Builder properties are nullable. An option that is not configured is omitted by Gson, so the server applies its own default. Set an option explicitly when client-side behavior must remain stable across server configurations.

Next: [[Client and endpoints|EN-Client-and-Endpoints]].

[[Français|FR-Getting-Started]]
