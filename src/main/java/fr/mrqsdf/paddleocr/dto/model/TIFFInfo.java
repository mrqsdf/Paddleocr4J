package fr.mrqsdf.paddleocr.dto.model;

/**
 * Métadonnées d'un document TIFF analysé.
 *
 * @param numPages nombre total de pages
 * @param pages dimensions de chaque page
 * @param type type du document, normalement {@code tiff}
 */
public record TIFFInfo(
        int numPages,
        PDFPageInfo[] pages,
        String type
) {

    /**
     * Crée les métadonnées d'un TIFF avec le type {@code tiff}.
     *
     * @param numPages nombre total de pages
     * @param pages dimensions de chaque page
     */
    public TIFFInfo(int numPages, PDFPageInfo[] pages) {
        this(numPages, pages, "tiff");
    }

}
