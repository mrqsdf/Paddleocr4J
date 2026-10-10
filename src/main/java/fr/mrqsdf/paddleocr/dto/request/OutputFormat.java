package fr.mrqsdf.paddleocr.dto.request;

/** Format d'export supplémentaire connu par l'API PaddleOCR. */
public enum OutputFormat {

    /** Document Microsoft Word au format DOCX. */
    DOCX("docx");

    /** Valeur textuelle transmise à l'API. */
    private final String value;

    /**
     * Associe une constante à sa représentation JSON.
     *
     * @param value valeur textuelle de l'API
     */
    OutputFormat(String value) {
        this.value = value;
    }

    /**
     * Retourne la valeur attendue par l'API.
     *
     * @return nom du format dans le contrat HTTP
     */
    public String value() {
        return value;
    }
}
