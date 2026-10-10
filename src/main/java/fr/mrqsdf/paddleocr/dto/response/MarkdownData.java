package fr.mrqsdf.paddleocr.dto.response;

import java.util.Map;

/**
 * Contenu Markdown généré pour une page.
 *
 * @param text texte Markdown
 * @param images images référencées dans le texte, indexées par leur nom
 */
public record MarkdownData(
        String text,
        Map<String, String> images
) {}
