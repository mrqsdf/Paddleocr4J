package fr.mrqsdf.paddleocr.dto.model;

/**
 * Métadonnées d'un document PDF analysé.
 *
 * @param numPages nombre total de pages
 * @param pages dimensions de chaque page
 * @param type type du document, normalement {@code pdf}
 */
public record PDFInfo(
        int numPages,
        PDFPageInfo[] pages,
        String type
) {
    /**
     * Crée les métadonnées d'un PDF avec le type {@code pdf}.
     *
     * @param numPages nombre total de pages
     * @param pages dimensions de chaque page
     */
    public PDFInfo(int numPages, PDFPageInfo[] pages) {
        this(numPages, pages, "pdf");
    }

}
