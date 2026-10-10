package fr.mrqsdf.paddleocr.dto.response;

/**
 * Réponse PaddleOCR ne contenant pas de résultat métier, notamment celle de santé.
 *
 * @param logId identifiant de corrélation de l'appel
 * @param errorCode code applicatif retourné par le serveur
 * @param errorMsg message associé au code applicatif
 */
public record AIStudioNoResultResponse(
        String logId,
        int errorCode,
        String errorMsg
) {
}
