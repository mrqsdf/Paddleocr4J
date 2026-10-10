package fr.mrqsdf.paddleocr.dto.request;

import com.google.gson.JsonObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Builder mutable d'une {@link Page} destinée à la restructuration.
 * Une instance doit être utilisée par un seul thread.
 */
public final class PageBuilder {

    /** Copie du résultat JSON élagué obligatoire. */
    private final JsonObject prunedResult;
    /** Images Markdown accumulées, conservées dans leur ordre d'ajout. */
    private final Map<String, String> markdownImages = new LinkedHashMap<>();

    /**
     * Crée un builder avec le résultat JSON obligatoire.
     *
     * @param prunedResult résultat JSON élagué d'une analyse
     * @throws NullPointerException si {@code prunedResult} vaut {@code null}
     */
    public PageBuilder(JsonObject prunedResult) {
        this.prunedResult = Objects.requireNonNull(
                prunedResult,
                "prunedResult ne peut pas être null"
        ).deepCopy();
    }

    /**
     * Ajoute ou remplace une image Markdown.
     *
     * @param name nom ou chemin utilisé dans le Markdown
     * @param content contenu encodé ou référence de l'image
     * @return ce builder
     * @throws IllegalArgumentException si le nom ou le contenu est vide
     */
    public PageBuilder markdownImage(String name, String content) {
        markdownImages.put(
                requireText(name, "name"),
                requireText(content, "content")
        );
        return this;
    }

    /**
     * Ajoute toutes les images Markdown d'une map.
     *
     * @param images images indexées par leur nom
     * @return ce builder
     * @throws NullPointerException si {@code images} vaut {@code null}
     */
    public PageBuilder markdownImages(Map<String, String> images) {
        Objects.requireNonNull(images, "images ne peut pas être null")
                .forEach(this::markdownImage);
        return this;
    }

    /**
     * Construit une page indépendante de l'état mutable du builder.
     *
     * @return nouvelle page validée
     */
    public Page build() {
        return new Page(
                prunedResult,
                markdownImages.isEmpty() ? null : markdownImages
        );
    }

    /**
     * Valide qu'une chaîne est renseignée.
     *
     * @param value valeur contrôlée
     * @param field nom du champ utilisé dans le message d'erreur
     * @return valeur validée
     * @throws IllegalArgumentException si la valeur est nulle ou vide
     */
    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " ne peut pas être vide");
        }
        return value;
    }
}
