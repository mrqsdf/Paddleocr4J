package fr.mrqsdf.paddleocr.dto.request;

import java.util.List;

/**
 * Requête de restructuration d'une ou plusieurs pages.
 *
 * @param outputFormats formats d'export supplémentaires demandés
 * @param pages pages à restructurer
 * @param mergeTables indique si les tableaux compatibles doivent être fusionnés
 * @param relevelTitles indique si les niveaux de titres doivent être recalculés
 * @param concatenatePages indique si les pages doivent être concaténées
 * @param prettifyMarkdown indique si le Markdown doit être embelli
 * @param showFormulaNumber indique si les numéros de formules doivent être conservés
 * @param returnMarkdownImages indique si les images Markdown doivent être renvoyées
 * @param logId identifiant de corrélation facultatif
 */
public record RestructurePagesRequest(
        List<String> outputFormats,
        List<Page> pages,

        Boolean mergeTables,
        Boolean relevelTitles,
        Boolean concatenatePages,
        Boolean prettifyMarkdown,
        Boolean showFormulaNumber,
        Boolean returnMarkdownImages,

        String logId
) {

    /**
     * Valide la requête et copie ses listes.
     *
     * @throws IllegalArgumentException si {@code pages} vaut {@code null}
     */
    public RestructurePagesRequest {
        if (pages == null) {
            throw new IllegalArgumentException(
                    "Le champ pages est obligatoire"
            );
        }

        pages = List.copyOf(pages);
        outputFormats = outputFormats == null ? null : List.copyOf(outputFormats);
    }

    /**
     * Crée une requête avec uniquement les pages obligatoires.
     *
     * @param pages pages à restructurer
     */
    public RestructurePagesRequest(List<Page> pages) {
        this(
                null,
                pages,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    /**
     * Crée un builder vide de requête de restructuration.
     *
     * @return nouveau builder
     */
    public static RestructurePagesRequestBuilder builder() {
        return new RestructurePagesRequestBuilder();
    }
}
