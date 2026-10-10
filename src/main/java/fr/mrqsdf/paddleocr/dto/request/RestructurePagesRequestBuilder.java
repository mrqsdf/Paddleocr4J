package fr.mrqsdf.paddleocr.dto.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Builder mutable d'une {@link RestructurePagesRequest}.
 * Une instance doit être utilisée par un seul thread.
 */
public final class RestructurePagesRequestBuilder {

    /** Formats d'export accumulés dans leur ordre d'ajout. */
    private final List<String> outputFormats = new ArrayList<>();
    /** Pages à restructurer dans leur ordre d'ajout. */
    private final List<Page> pages = new ArrayList<>();
    /** Option de fusion des tableaux. */
    private Boolean mergeTables;
    /** Option de recalcul des niveaux de titres. */
    private Boolean relevelTitles;
    /** Option de concaténation des pages. */
    private Boolean concatenatePages;
    /** Option d'embellissement du Markdown. */
    private Boolean prettifyMarkdown;
    /** Option d'affichage des numéros de formules. */
    private Boolean showFormulaNumber;
    /** Option de retour des images Markdown. */
    private Boolean returnMarkdownImages;
    /** Identifiant de corrélation facultatif. */
    private String logId;

    /** Crée un builder vide de requête de restructuration. */
    public RestructurePagesRequestBuilder() {
    }

    /**
     * Ajoute un format d'export connu.
     *
     * @param outputFormat format à ajouter
     * @return ce builder
     */
    public RestructurePagesRequestBuilder outputFormat(OutputFormat outputFormat) {
        return outputFormat(Objects.requireNonNull(outputFormat, "outputFormat ne peut pas être null").value());
    }

    /**
     * Ajoute un format d'export brut, notamment pour une future valeur de l'API.
     *
     * @param outputFormat nom du format
     * @return ce builder
     */
    public RestructurePagesRequestBuilder outputFormat(String outputFormat) {
        outputFormats.add(requireText(outputFormat, "outputFormat"));
        return this;
    }

    /**
     * Ajoute plusieurs formats d'export connus.
     *
     * @param outputFormats formats à ajouter
     * @return ce builder
     */
    public RestructurePagesRequestBuilder outputFormats(OutputFormat... outputFormats) {
        Objects.requireNonNull(outputFormats, "outputFormats ne peut pas être null");
        Arrays.stream(outputFormats).forEach(this::outputFormat);
        return this;
    }

    /**
     * Ajoute plusieurs formats d'export bruts.
     *
     * @param outputFormats noms des formats
     * @return ce builder
     */
    public RestructurePagesRequestBuilder outputFormats(String... outputFormats) {
        Objects.requireNonNull(outputFormats, "outputFormats ne peut pas être null");
        Arrays.stream(outputFormats).forEach(this::outputFormat);
        return this;
    }

    /**
     * Ajoute une page à restructurer.
     *
     * @param page page à ajouter
     * @return ce builder
     */
    public RestructurePagesRequestBuilder page(Page page) {
        pages.add(Objects.requireNonNull(page, "page ne peut pas être null"));
        return this;
    }

    /**
     * Ajoute plusieurs pages à restructurer.
     *
     * @param pages pages à ajouter
     * @return ce builder
     */
    public RestructurePagesRequestBuilder pages(Page... pages) {
        Objects.requireNonNull(pages, "pages ne peut pas être null");
        Arrays.stream(pages).forEach(this::page);
        return this;
    }

    /**
     * Ajoute toutes les pages d'un itérable.
     *
     * @param pages pages à ajouter
     * @return ce builder
     */
    public RestructurePagesRequestBuilder pages(Iterable<Page> pages) {
        Objects.requireNonNull(pages, "pages ne peut pas être null");
        pages.forEach(this::page);
        return this;
    }

    /**
     * Configure la fusion des tableaux.
     *
     * @param value état demandé
     * @return ce builder
     */
    public RestructurePagesRequestBuilder mergeTables(boolean value) {
        this.mergeTables = value;
        return this;
    }

    /**
     * Configure le recalcul des niveaux de titres.
     *
     * @param value état demandé
     * @return ce builder
     */
    public RestructurePagesRequestBuilder relevelTitles(boolean value) {
        this.relevelTitles = value;
        return this;
    }

    /**
     * Configure la concaténation des pages.
     *
     * @param value état demandé
     * @return ce builder
     */
    public RestructurePagesRequestBuilder concatenatePages(boolean value) {
        this.concatenatePages = value;
        return this;
    }

    /**
     * Configure l'embellissement du Markdown.
     *
     * @param value état demandé
     * @return ce builder
     */
    public RestructurePagesRequestBuilder prettifyMarkdown(boolean value) {
        this.prettifyMarkdown = value;
        return this;
    }

    /**
     * Configure l'affichage des numéros de formules.
     *
     * @param value état demandé
     * @return ce builder
     */
    public RestructurePagesRequestBuilder showFormulaNumber(boolean value) {
        this.showFormulaNumber = value;
        return this;
    }

    /**
     * Configure le retour des images Markdown.
     *
     * @param value état demandé
     * @return ce builder
     */
    public RestructurePagesRequestBuilder returnMarkdownImages(boolean value) {
        this.returnMarkdownImages = value;
        return this;
    }

    /**
     * Définit l'identifiant de corrélation.
     *
     * @param value identifiant transmis au serveur
     * @return ce builder
     */
    public RestructurePagesRequestBuilder logId(String value) {
        this.logId = value;
        return this;
    }

    /**
     * Construit une requête indépendante de l'état mutable du builder.
     *
     * @return nouvelle requête de restructuration
     */
    public RestructurePagesRequest build() {
        return new RestructurePagesRequest(
                outputFormats.isEmpty() ? null : List.copyOf(outputFormats),
                List.copyOf(pages),
                mergeTables,
                relevelTitles,
                concatenatePages,
                prettifyMarkdown,
                showFormulaNumber,
                returnMarkdownImages,
                logId
        );
    }

    /**
     * Valide qu'une chaîne est renseignée.
     *
     * @param value valeur contrôlée
     * @param field nom du champ utilisé dans le message d'erreur
     * @return valeur validée
     */
    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " ne peut pas être vide");
        }
        return value;
    }
}
