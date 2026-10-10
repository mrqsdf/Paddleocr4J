package fr.mrqsdf.paddleocr.dto.request;

import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Page transmise à l'endpoint de restructuration.
 *
 * @param prunedResult résultat JSON élagué produit par une analyse
 * @param markdownImages images référencées par le Markdown de la page
 */
public record Page(
        JsonObject prunedResult,
        Map<String, String> markdownImages
) {

    /**
     * Valide la page et effectue des copies défensives de ses données mutables.
     *
     * @throws IllegalArgumentException si {@code prunedResult} vaut {@code null}
     */
    public Page {
        if (prunedResult == null) {
            throw new IllegalArgumentException(
                    "Le champ prunedResult est obligatoire"
            );
        }
        prunedResult = prunedResult.deepCopy();
        markdownImages = markdownImages == null
                ? null
                : Collections.unmodifiableMap(new LinkedHashMap<>(markdownImages));
    }

    /**
     * Crée une page sans image Markdown.
     *
     * @param prunedResult résultat JSON élagué
     */
    public Page(JsonObject prunedResult) {
        this(prunedResult, null);
    }

    /**
     * Crée un builder initialisé avec le résultat JSON obligatoire.
     *
     * @param prunedResult résultat JSON élagué
     * @return nouveau builder de page
     */
    public static PageBuilder builder(JsonObject prunedResult) {
        return new PageBuilder(prunedResult);
    }
}
