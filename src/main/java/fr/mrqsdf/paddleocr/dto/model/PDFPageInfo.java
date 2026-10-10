package fr.mrqsdf.paddleocr.dto.model;

/**
 * Dimensions d'une page de PDF ou de TIFF.
 *
 * @param width largeur de la page en pixels
 * @param height hauteur de la page en pixels
 */
public record PDFPageInfo(
        int width,
        int height
) {
}
