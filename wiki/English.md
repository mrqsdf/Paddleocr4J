# Paddleocr4J documentation

Paddleocr4J is a synchronous, thread-safe Java client for a PaddleOCR HTTP service compatible with the repository's `openapi.json` contract.

It supports the full workflow:

1. check server health;
2. analyze an image, PDF, or other server-supported document;
3. read Markdown, images, structured data, and exports;
4. optionally send analyzed pages to the restructuring endpoint.

## Maven Central

```groovy
dependencies {
    implementation 'fr.mrqsdf:PaddleOCR4J:1.0.0'
}
```

## Documentation map

- [[Getting started|EN-Getting-Started]]: requirements, build, dependency setup, and first request.
- [[Client and endpoints|EN-Client-and-Endpoints]]: client lifecycle and the three HTTP operations.
- [[Request builders|EN-Request-Builders]]: every request option, typed enums, and raw values.
- [[Responses and errors|EN-Responses-and-Errors]]: response DTOs, application errors, and exceptions.
- [[Thread safety|EN-Thread-Safety]]: safe concurrent use and builder ownership.
- [[Development|EN-Development]]: tests, Javadoc, structure, and OpenAPI updates.

For a compact overview, see the repository README. For exact Java signatures, generate the Javadoc with `./gradlew javadoc`.

[[Français|Francais]]
