package fr.mrqsdf.paddleocr.dto.response;

import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Résultat de l'analyse de mise en page d'une page ou d'une image.
 *
 * @param prunedResult résultat JSON élagué réutilisable pour la restructuration
 * @param markdown contenu Markdown produit
 * @param outputImages images générées, indexées par leur nom
 * @param inputImage représentation de l'image d'entrée éventuellement renvoyée
 * @param exports documents exportés éventuels
 */
public record LayoutParsingResult(
        JsonObject prunedResult,
        MarkdownData markdown,
        Map<String, String> outputImages,
        String inputImage,
        DocumentExports exports
) {}
