package fr.mrqsdf.paddleocr.dto.response;

/**
 * Réponse PaddleOCR contenant un résultat métier typé.
 *
 * @param logId identifiant de corrélation de l'appel
 * @param result résultat produit par l'opération
 * @param errorCode code applicatif retourné par le serveur
 * @param errorMsg message associé au code applicatif
 * @param <T> type du résultat métier
 */
public record AIStudioResponse<T>(
        String logId,
        T result,
        int errorCode,
        String errorMsg
) {

    /**
     * Indique si le serveur a retourné le code applicatif de succès.
     *
     * @return {@code true} lorsque {@code errorCode} vaut {@code 0}
     */
    public boolean isSuccess() {
        return errorCode == 0;
    }
}
