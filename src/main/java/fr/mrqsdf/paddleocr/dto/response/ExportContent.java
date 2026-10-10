package fr.mrqsdf.paddleocr.dto.response;

/**
 * Contenu d'un fichier exporté par PaddleOCR.
 *
 * @param content contenu encodé ou référence retournée par le serveur
 * @param fileName nom du fichier exporté
 */
public record ExportContent(
        String content,
        String fileName
) {}
