# Development

## Repository layout

```text
Paddleocr4J/
├── openapi.json
├── build.gradle
├── README.md
├── wiki/en and wiki/fr
└── src/
    ├── main/java/fr/mrqsdf/paddleocr/
    │   ├── PaddleOCRClient.java
    │   ├── PaddleOCRException.java
    │   ├── HttpClientUtil.java
    │   └── dto/{model,request,response}
    └── test/java/
```

`PaddleOCRClient` is the public transport facade. `HttpClientUtil` owns HTTP and JSON processing. Request DTOs, builders, and enums live in `dto.request`; API results live in `dto.response`; document metadata lives in `dto.model`.

## Run checks

```bash
./gradlew clean test javadoc
```

Windows:

```powershell
.\gradlew.bat clean test javadoc
```

Outputs:

- test report: `build/reports/tests/test/index.html`;
- Javadoc: `build/docs/javadoc/index.html`;
- JAR: `build/libs/Paddleocr4J-1.0-SNAPSHOT.jar`.

Tests exercise all endpoints through a local HTTP server, generic response deserialization, raw and typed builder values, HTTP error details, and concurrent use of one client.

## Updating the OpenAPI contract

1. Replace or compare `openapi.json` with the server contract.
2. Review changed paths, methods, request fields, defaults, and response schemas.
3. Update DTOs and builders for structural changes.
4. Add stable documented values to enums.
5. Keep raw overloads unless the server removes free-form support.
6. Add focused serialization or HTTP integration tests.
7. Update the English README and both wiki languages.
8. Run the complete checks.

Raw overloads often allow a new enum value to work before a release. New fields and changed JSON structures require code changes.

## Publishing this wiki on GitHub

The `wiki` directory is ready to be copied to the repository's separate GitHub Wiki repository. `Home.md` is the language selector and `_Sidebar.md` provides navigation. Keep the `en` and `fr` folders when the target wiki workflow supports nested files. If a publishing tool flattens page names, preserve language prefixes in the destination names to avoid collisions.

[[Français|FR-Development]]
