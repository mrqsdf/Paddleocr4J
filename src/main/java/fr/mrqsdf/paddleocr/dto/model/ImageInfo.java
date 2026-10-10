package fr.mrqsdf.paddleocr.dto.model;

/**
 * Métadonnées d'une image analysée.
 *
 * @param width largeur de l'image en pixels
 * @param height hauteur de l'image en pixels
 * @param type type du document, normalement {@code image}
 */
public record ImageInfo(
        int width,
        int height,
        String type
) {
    /**
     * Crée les métadonnées d'une image avec le type {@code image}.
     *
     * @param width largeur en pixels
     * @param height hauteur en pixels
     */
    public ImageInfo(int width, int height) {
        this(width, height, "image");
    }

}
