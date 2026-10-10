package fr.mrqsdf.paddleocr.dto.response;

import java.util.List;

/**
 * Résultat de la restructuration d'un ensemble de pages.
 *
 * @param layoutParsingResults pages après restructuration
 */
public record RestructurePagesResult(
        List<LayoutParsingResult> layoutParsingResults
) {}
