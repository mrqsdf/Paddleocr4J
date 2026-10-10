package fr.mrqsdf.paddleocr.dto.request;

/** Type de fichier d'entrée connu par l'API PaddleOCR. */
public enum InputFileType {

    /** Document PDF, représenté par la valeur API {@code 0}. */
    PDF(0),
    /** Image, représentée par la valeur API {@code 1}. */
    IMAGE(1);

    /** Valeur numérique transmise à l'API. */
    private final int value;

    /**
     * Associe une constante à sa valeur dans le contrat HTTP.
     *
     * @param value valeur numérique de l'API
     */
    InputFileType(int value) {
        this.value = value;
    }

    /**
     * Retourne la valeur attendue par l'API.
     *
     * @return code numérique du type de fichier
     */
    public int value() {
        return value;
    }
}
