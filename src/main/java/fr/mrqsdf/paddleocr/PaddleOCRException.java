package fr.mrqsdf.paddleocr;

/**
 * Exception contrôlée levée lorsqu'un appel à PaddleOCR ne peut pas aboutir.
 * Pour une erreur HTTP, le statut et le corps brut de la réponse sont conservés.
 */
public class PaddleOCRException extends Exception {

    /** Statut HTTP reçu, ou {@code null} si aucune réponse HTTP n'a été obtenue. */
    private final Integer statusCode;
    /** Corps brut de la réponse HTTP en erreur, ou {@code null}. */
    private final String responseBody;

    /**
     * Crée une exception sans cause ni réponse HTTP.
     *
     * @param message description de l'erreur
     */
    public PaddleOCRException(String message) {
        this(message, null, null, null);
    }

    /**
     * Crée une exception associée à une cause.
     *
     * @param message description de l'erreur
     * @param cause erreur d'origine
     */
    public PaddleOCRException(String message, Throwable cause) {
        this(message, cause, null, null);
    }

    /**
     * Crée une exception à partir d'une réponse HTTP en erreur.
     *
     * @param message description de l'erreur
     * @param statusCode statut HTTP reçu
     * @param responseBody corps brut de la réponse
     */
    public PaddleOCRException(String message, int statusCode, String responseBody) {
        this(message, null, statusCode, responseBody);
    }

    /**
     * Constructeur commun à toutes les variantes de l'exception.
     *
     * @param message description de l'erreur
     * @param cause erreur d'origine éventuelle
     * @param statusCode statut HTTP éventuel
     * @param responseBody corps HTTP éventuel
     */
    private PaddleOCRException(
            String message,
            Throwable cause,
            Integer statusCode,
            String responseBody
    ) {
        super(message, cause);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    /**
     * Retourne le statut HTTP associé à l'erreur.
     *
     * @return statut HTTP, ou {@code null} en l'absence de réponse HTTP
     */
    public Integer getStatusCode() {
        return statusCode;
    }

    /**
     * Retourne le corps brut reçu lors d'une erreur HTTP.
     *
     * @return corps brut, ou {@code null} en l'absence de réponse HTTP
     */
    public String getResponseBody() {
        return responseBody;
    }
}
