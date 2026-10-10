package fr.mrqsdf.paddleocr;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;

/** Utilitaire interne chargé du transport HTTP et de la conversion JSON. */
final class HttpClientUtil {

    /** Durée maximale d'un appel HTTP complet. */
    private static final Duration REQUEST_TIMEOUT = Duration.ofMinutes(5);

    /** Client HTTP thread-safe partagé par toutes les instances de client PaddleOCR. */
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    /** Convertisseur JSON thread-safe partagé par tous les appels. */
    private static final Gson GSON = new Gson();

    /** Empêche l'instanciation de cette classe utilitaire. */
    private HttpClientUtil() {
    }

    /**
     * Exécute une requête HTTP GET et désérialise sa réponse JSON.
     *
     * @param uri URI appelée
     * @param responseType type de réponse attendu
     * @param <T> type Java retourné
     * @return réponse désérialisée
     * @throws PaddleOCRException si le transport ou la désérialisation échoue
     */
    static <T> T get(URI uri, Type responseType) throws PaddleOCRException {
        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(REQUEST_TIMEOUT)
                .header("Accept", "application/json")
                .GET()
                .build();

        return send(request, responseType);
    }

    /**
     * Sérialise un corps, exécute une requête HTTP POST et désérialise sa réponse.
     *
     * @param uri URI appelée
     * @param requestBody objet à sérialiser en JSON
     * @param responseType type de réponse attendu
     * @param <T> type Java retourné
     * @return réponse désérialisée
     * @throws PaddleOCRException si la sérialisation, le transport ou la désérialisation échoue
     */
    static <T> T post(URI uri, Object requestBody, Type responseType) throws PaddleOCRException {
        Objects.requireNonNull(requestBody, "requestBody ne peut pas être null");

        final String json;
        try {
            json = GSON.toJson(requestBody);
        } catch (RuntimeException exception) {
            throw new PaddleOCRException("Impossible de sérialiser la requête", exception);
        }

        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(REQUEST_TIMEOUT)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

        return send(request, responseType);
    }

    /**
     * Envoie une requête préparée, valide son statut et convertit son corps JSON.
     *
     * @param request requête HTTP prête à être envoyée
     * @param responseType type de réponse attendu
     * @param <T> type Java retourné
     * @return réponse désérialisée
     * @throws PaddleOCRException en cas d'interruption, d'erreur réseau, de statut non-2xx ou de JSON invalide
     */
    private static <T> T send(HttpRequest request, Type responseType) throws PaddleOCRException {
        Objects.requireNonNull(responseType, "responseType ne peut pas être null");

        final HttpResponse<String> response;
        try {
            response = HTTP_CLIENT.send(
                    request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
            );
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new PaddleOCRException("Appel à PaddleOCR interrompu", exception);
        } catch (IOException exception) {
            throw new PaddleOCRException("Échec de l'appel à PaddleOCR", exception);
        }

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new PaddleOCRException(
                    "PaddleOCR a répondu avec le statut HTTP " + response.statusCode(),
                    response.statusCode(),
                    response.body()
            );
        }

        try {
            T result = GSON.fromJson(response.body(), responseType);
            if (result == null) {
                throw new PaddleOCRException("PaddleOCR a renvoyé une réponse JSON vide");
            }
            return result;
        } catch (JsonParseException exception) {
            throw new PaddleOCRException("Réponse JSON PaddleOCR invalide", exception);
        }
    }

}
