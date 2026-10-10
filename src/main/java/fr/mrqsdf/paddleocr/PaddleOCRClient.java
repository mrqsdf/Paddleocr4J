package fr.mrqsdf.paddleocr;

import com.google.gson.reflect.TypeToken;
import fr.mrqsdf.paddleocr.dto.request.InferRequest;
import fr.mrqsdf.paddleocr.dto.request.RestructurePagesRequest;
import fr.mrqsdf.paddleocr.dto.response.AIStudioNoResultResponse;
import fr.mrqsdf.paddleocr.dto.response.AIStudioResponse;
import fr.mrqsdf.paddleocr.dto.response.InferResult;
import fr.mrqsdf.paddleocr.dto.response.RestructurePagesResult;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * Client synchrone et thread-safe de l'API HTTP PaddleOCR.
 * Une même instance peut être partagée entre plusieurs threads.
 */
public final class PaddleOCRClient {

    /** Chemin relatif de l'endpoint de santé. */
    private static final String HEALTH = "health";
    /** Chemin relatif de l'endpoint d'analyse de document. */
    private static final String LAYOUT_PARSING = "layout-parsing";
    /** Chemin relatif de l'endpoint de restructuration des pages. */
    private static final String RESTRUCTURE_PAGES = "restructure-pages";

    /** Type Gson complet de la réponse d'analyse. */
    private static final Type INFER_RESPONSE_TYPE = TypeToken.getParameterized(
            AIStudioResponse.class,
            InferResult.class
    ).getType();

    /** Type Gson complet de la réponse de restructuration. */
    private static final Type RESTRUCTURE_RESPONSE_TYPE = TypeToken.getParameterized(
            AIStudioResponse.class,
            RestructurePagesResult.class
    ).getType();

    /** URI de base normalisée avec une barre oblique finale. */
    private final URI baseUri;

    /**
     * Crée un client à partir d'une URI déjà validée.
     *
     * @param baseUri URI de base du service
     */
    private PaddleOCRClient(URI baseUri) {
        this.baseUri = normalizeBaseUri(baseUri);
    }

    /**
     * Initialise un client PaddleOCR.
     *
     * @param url URL HTTP ou HTTPS absolue du service
     * @return nouveau client thread-safe
     * @throws PaddleOCRException si l'URL est invalide ou utilise un protocole non pris en charge
     * @throws NullPointerException si {@code url} vaut {@code null}
     */
    public static PaddleOCRClient init(String url) throws PaddleOCRException {
        Objects.requireNonNull(url, "url ne peut pas être null");

        try {
            URI uri = new URI(url);
            if (!uri.isAbsolute() || uri.getHost() == null) {
                throw new PaddleOCRException("L'URL PaddleOCR doit être une URL HTTP(S) absolue");
            }
            if (!"http".equalsIgnoreCase(uri.getScheme())
                    && !"https".equalsIgnoreCase(uri.getScheme())) {
                throw new PaddleOCRException("L'URL PaddleOCR doit utiliser HTTP ou HTTPS");
            }
            return new PaddleOCRClient(uri);
        } catch (URISyntaxException exception) {
            throw new PaddleOCRException("URL PaddleOCR invalide : " + url, exception);
        }
    }

    /**
     * Vérifie l'état du service PaddleOCR.
     *
     * @return réponse de l'endpoint {@code /health}
     * @throws PaddleOCRException si l'appel HTTP ou la lecture de la réponse échoue
     */
    public AIStudioNoResultResponse getHealth() throws PaddleOCRException {
        return HttpClientUtil.get(endpoint(HEALTH), AIStudioNoResultResponse.class);
    }

    /**
     * Analyse une image, un PDF ou un document pris en charge par le serveur.
     *
     * @param request paramètres et contenu du document à analyser
     * @return réponse contenant les résultats d'analyse
     * @throws PaddleOCRException si l'appel HTTP ou la lecture de la réponse échoue
     * @throws NullPointerException si {@code request} vaut {@code null}
     */
    public AIStudioResponse<InferResult> infer(InferRequest request) throws PaddleOCRException {
        Objects.requireNonNull(request, "request ne peut pas être null");
        return HttpClientUtil.post(endpoint(LAYOUT_PARSING), request, INFER_RESPONSE_TYPE);
    }

    /**
     * Restructure une ou plusieurs pages issues d'une analyse.
     *
     * @param request pages et options de restructuration
     * @return réponse contenant les pages restructurées
     * @throws PaddleOCRException si l'appel HTTP ou la lecture de la réponse échoue
     * @throws NullPointerException si {@code request} vaut {@code null}
     */
    public AIStudioResponse<RestructurePagesResult> restructurePages(
            RestructurePagesRequest request
    ) throws PaddleOCRException {
        Objects.requireNonNull(request, "request ne peut pas être null");
        return HttpClientUtil.post(endpoint(RESTRUCTURE_PAGES), request, RESTRUCTURE_RESPONSE_TYPE);
    }

    /**
     * Résout le chemin d'un endpoint par rapport à l'URI de base.
     *
     * @param path chemin relatif de l'endpoint
     * @return URI complète de l'endpoint
     */
    private URI endpoint(String path) {
        return baseUri.resolve(path);
    }

    /**
     * Ajoute une barre oblique finale à l'URI lorsqu'elle est absente.
     *
     * @param uri URI à normaliser
     * @return URI normalisée
     */
    private static URI normalizeBaseUri(URI uri) {
        String value = uri.toString();
        return URI.create(value.endsWith("/") ? value : value + "/");
    }
}
