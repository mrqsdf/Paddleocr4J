package fr.mrqsdf.paddleocr.dto.response;

import com.google.gson.JsonObject;

import java.util.List;

/**
 * Résultat complet de l'analyse d'un document.
 *
 * @param layoutParsingResults résultats de mise en page par page ou image
 * @param dataInfo métadonnées JSON du document d'entrée
 */
public record InferResult(
        List<LayoutParsingResult> layoutParsingResults,
        JsonObject dataInfo
) {}
