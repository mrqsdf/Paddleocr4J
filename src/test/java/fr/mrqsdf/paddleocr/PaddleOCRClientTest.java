package fr.mrqsdf.paddleocr;

import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import fr.mrqsdf.paddleocr.dto.request.InferRequest;
import fr.mrqsdf.paddleocr.dto.request.InputFileType;
import fr.mrqsdf.paddleocr.dto.request.LayoutShapeMode;
import fr.mrqsdf.paddleocr.dto.request.Page;
import fr.mrqsdf.paddleocr.dto.request.RestructurePagesRequest;
import fr.mrqsdf.paddleocr.dto.response.AIStudioResponse;
import fr.mrqsdf.paddleocr.dto.response.InferResult;
import fr.mrqsdf.paddleocr.dto.response.RestructurePagesResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaddleOCRClientTest {

    private HttpServer server;
    private ExecutorService serverExecutor;
    private PaddleOCRClient client;

    @BeforeEach
    void setUp() throws Exception {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        serverExecutor = Executors.newCachedThreadPool();
        server.setExecutor(serverExecutor);
        server.start();
        client = PaddleOCRClient.init("http://localhost:" + server.getAddress().getPort());
    }

    @AfterEach
    void tearDown() {
        server.stop(0);
        serverExecutor.shutdownNow();
    }

    @Test
    void callsEveryEndpointAndDeserializesGenericResults() throws Exception {
        AtomicReference<String> inferBody = new AtomicReference<>();

        server.createContext("/health", exchange -> respond(
                exchange,
                200,
                "{\"logId\":\"health-1\",\"errorCode\":0,\"errorMsg\":\"Success\"}"
        ));
        server.createContext("/layout-parsing", exchange -> {
            inferBody.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            respond(exchange, 200, """
                    {"logId":"infer-1","result":{"layoutParsingResults":[],
                    "dataInfo":{"width":100,"height":50,"type":"image"}},
                    "errorCode":0,"errorMsg":"Success"}
                    """);
        });
        server.createContext("/restructure-pages", exchange -> respond(exchange, 200, """
                {"logId":"restructure-1","result":{"layoutParsingResults":[]},
                "errorCode":0,"errorMsg":"Success"}
                """));

        assertEquals("health-1", client.getHealth().logId());

        AIStudioResponse<InferResult> inferResponse = client.infer(inferRequest());
        assertEquals("infer-1", inferResponse.logId());
        assertEquals(100, inferResponse.result().dataInfo().get("width").getAsInt());
        assertEquals(
                "quad",
                JsonParser.parseString(inferBody.get()).getAsJsonObject().get("layoutShapeMode").getAsString()
        );

        Page page = new Page(JsonParser.parseString("{}").getAsJsonObject());
        AIStudioResponse<RestructurePagesResult> restructureResponse =
                client.restructurePages(new RestructurePagesRequest(List.of(page)));
        assertEquals("restructure-1", restructureResponse.logId());
        assertTrue(restructureResponse.result().layoutParsingResults().isEmpty());
    }

    @Test
    void oneClientInstanceSupportsConcurrentCalls() throws Exception {
        server.createContext("/health", exchange -> respond(
                exchange,
                200,
                "{\"logId\":\"shared\",\"errorCode\":0,\"errorMsg\":\"Success\"}"
        ));

        try (ExecutorService callers = Executors.newFixedThreadPool(12)) {
            List<Future<String>> calls = java.util.stream.IntStream.range(0, 100)
                    .mapToObj(index -> callers.submit(() -> client.getHealth().logId()))
                    .toList();

            for (Future<String> call : calls) {
                assertEquals("shared", call.get());
            }
        }
    }

    @Test
    void exposesHttpFailureDetails() throws Exception {
        server.createContext("/health", exchange -> respond(
                exchange,
                503,
                "{\"errorCode\":503,\"errorMsg\":\"Unavailable\"}"
        ));

        try {
            client.getHealth();
        } catch (PaddleOCRException exception) {
            assertEquals(503, exception.getStatusCode());
            assertTrue(exception.getResponseBody().contains("Unavailable"));
            return;
        }

        throw new AssertionError("Une PaddleOCRException était attendue");
    }

    private static InferRequest inferRequest() {
        return InferRequest.builder("base64-content")
                .fileType(InputFileType.IMAGE)
                .layoutShapeMode(LayoutShapeMode.QUAD)
                .build();
    }

    private static void respond(HttpExchange exchange, int status, String body) throws IOException {
        assertInstanceOf(String.class, exchange.getRequestMethod());
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }
}
