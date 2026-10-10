# Paddleocr4J documentation

Paddleocr4J is a synchronous, thread-safe Java client for a PaddleOCR HTTP service compatible with the repository's `openapi.json` contract.

It supports the full workflow:

1. check server health;
2. analyze an image, PDF, or other server-supported document;
3. read Markdown, images, structured data, and exports;
4. optionally send analyzed pages to the restructuring endpoint.

## Documentation map

- [Getting started](Getting-Started.md): requirements, build, dependency setup, and first request.
- [Client and endpoints](Client-and-Endpoints.md): client lifecycle and the three HTTP operations.
- [Request builders](Request-Builders.md): every request option, typed enums, and raw values.
- [Responses and errors](Responses-and-Errors.md): response DTOs, application errors, and exceptions.
- [Thread safety](Thread-Safety.md): safe concurrent use and builder ownership.
- [Development](Development.md): tests, Javadoc, structure, and OpenAPI updates.

For a compact overview, see the repository [README](../../README.md). For exact Java signatures, generate the Javadoc with `./gradlew javadoc`.

[Français](../fr/Home.md)
