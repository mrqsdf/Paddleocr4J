package fr.mrqsdf.paddleocr.dto.request;

/** Forme géométrique demandée pour les zones détectées. */
public enum LayoutShapeMode {

    /** Rectangle aligné sur les axes. */
    RECT("rect"),
    /** Quadrilatère à quatre sommets. */
    QUAD("quad"),
    /** Polygone libre. */
    POLY("poly"),
    /** Choix automatique effectué par le serveur. */
    AUTO("auto");

    /** Valeur textuelle transmise à l'API. */
    private final String value;

    /**
     * Associe une constante à sa représentation JSON.
     *
     * @param value valeur textuelle de l'API
     */
    LayoutShapeMode(String value) {
        this.value = value;
    }

    /**
     * Retourne la valeur attendue par l'API.
     *
     * @return représentation JSON du mode
     */
    public String value() {
        return value;
    }
}
