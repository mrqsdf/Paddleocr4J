package fr.mrqsdf.paddleocr.dto.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Builder mutable d'une {@link InferRequest}.
 * Les champs non configurés restent à {@code null} et sont omis du JSON par Gson.
 * Une instance doit être utilisée par un seul thread.
 */
public final class InferRequestBuilder {

    /** Convertisseur utilisé pour transformer les maps de configuration en JSON. */
    private static final Gson GSON = new Gson();

    /** Contenu ou référence obligatoire du document. */
    private final String file;
    /** Type numérique facultatif du fichier. */
    private Integer fileType;
    /** Formats d'export supplémentaires accumulés. */
    private final List<String> outputFormats = new ArrayList<>();
    /** Activation de la classification d'orientation. */
    private Boolean useDocOrientationClassify;
    /** Activation du redressement du document. */
    private Boolean useDocUnwarping;
    /** Activation de la détection de mise en page. */
    private Boolean useLayoutDetection;
    /** Activation de la reconnaissance des graphiques. */
    private Boolean useChartRecognition;
    /** Activation de la reconnaissance des sceaux. */
    private Boolean useSealRecognition;
    /** Activation de l'OCR dans les blocs image. */
    private Boolean useOcrForImageBlock;
    /** Seuil commun ou seuils de layout par catégorie. */
    private JsonElement layoutThreshold;
    /** Activation du NMS de mise en page. */
    private Boolean layoutNms;
    /** Ratio de déclippage commun, par axe ou par catégorie. */
    private JsonElement layoutUnclipRatio;
    /** Mode de fusion des boîtes commun ou par catégorie. */
    private JsonElement layoutMergeBboxesMode;
    /** Forme textuelle des zones de layout. */
    private String layoutShapeMode;
    /** Label ou instruction transmis au modèle. */
    private String promptLabel;
    /** Activation du formatage du contenu des blocs. */
    private Boolean formatBlockContent;
    /** Pénalité de répétition du modèle. */
    private Double repetitionPenalty;
    /** Température d'échantillonnage du modèle. */
    private Double temperature;
    /** Seuil d'échantillonnage top-p. */
    private Double topP;
    /** Nombre minimal de pixels. */
    private Integer minPixels;
    /** Nombre maximal de pixels. */
    private Integer maxPixels;
    /** Nombre maximal de nouveaux tokens. */
    private Integer maxNewTokens;
    /** Activation de la fusion des blocs de mise en page. */
    private Boolean mergeLayoutBlocks;
    /** Labels à ignorer lors de la production du Markdown. */
    private final List<String> markdownIgnoreLabels = new ArrayList<>();
    /** Arguments supplémentaires du modèle VLM. */
    private Map<String, Object> vlmExtraArgs;
    /** Activation de l'embellissement du Markdown. */
    private Boolean prettifyMarkdown;
    /** Activation de l'affichage des numéros de formules. */
    private Boolean showFormulaNumber;
    /** Activation du retour des images Markdown. */
    private Boolean returnMarkdownImages;
    /** Activation de la restructuration pendant l'analyse. */
    private Boolean restructurePages;
    /** Activation de la fusion des tableaux. */
    private Boolean mergeTables;
    /** Activation du recalcul des niveaux de titres. */
    private Boolean relevelTitles;
    /** Activation des sorties de visualisation. */
    private Boolean visualize;
    /** Identifiant de corrélation facultatif. */
    private String logId;

    /**
     * Crée un builder pour le document indiqué.
     *
     * @param file contenu ou référence du document
     * @throws IllegalArgumentException si {@code file} est nul ou vide
     */
    public InferRequestBuilder(String file) {
        this.file = requireText(file, "file");
    }

    /**
     * Définit un type de fichier connu.
     * @param fileType type de fichier
     * @return ce builder
     */
    public InferRequestBuilder fileType(InputFileType fileType) {
        this.fileType = Objects.requireNonNull(fileType, "fileType ne peut pas être null").value();
        return this;
    }

    /**
     * Définit un type de fichier numérique brut.
     * @param fileType code transmis au serveur
     * @return ce builder
     */
    public InferRequestBuilder fileType(int fileType) {
        this.fileType = fileType;
        return this;
    }

    /**
     * Ajoute un format d'export connu.
     * @param outputFormat format à ajouter
     * @return ce builder
     */
    public InferRequestBuilder outputFormat(OutputFormat outputFormat) {
        return outputFormat(Objects.requireNonNull(outputFormat, "outputFormat ne peut pas être null").value());
    }

    /**
     * Ajoute un format d'export brut.
     * @param outputFormat nom du format
     * @return ce builder
     */
    public InferRequestBuilder outputFormat(String outputFormat) {
        outputFormats.add(requireText(outputFormat, "outputFormat"));
        return this;
    }

    /**
     * Ajoute plusieurs formats d'export connus.
     * @param outputFormats formats à ajouter
     * @return ce builder
     */
    public InferRequestBuilder outputFormats(OutputFormat... outputFormats) {
        Objects.requireNonNull(outputFormats, "outputFormats ne peut pas être null");
        Arrays.stream(outputFormats).forEach(this::outputFormat);
        return this;
    }

    /**
     * Ajoute plusieurs formats d'export bruts.
     * @param outputFormats noms des formats
     * @return ce builder
     */
    public InferRequestBuilder outputFormats(String... outputFormats) {
        Objects.requireNonNull(outputFormats, "outputFormats ne peut pas être null");
        Arrays.stream(outputFormats).forEach(this::outputFormat);
        return this;
    }

    /**
     * Configure la classification de l'orientation du document.
     * @param value état demandé
     * @return ce builder
     */
    public InferRequestBuilder useDocOrientationClassify(boolean value) {
        this.useDocOrientationClassify = value;
        return this;
    }

    /**
     * Configure le redressement du document.
     * @param value état demandé
     * @return ce builder
     */
    public InferRequestBuilder useDocUnwarping(boolean value) {
        this.useDocUnwarping = value;
        return this;
    }

    /**
     * Configure la détection de mise en page.
     * @param value état demandé
     * @return ce builder
     */
    public InferRequestBuilder useLayoutDetection(boolean value) {
        this.useLayoutDetection = value;
        return this;
    }

    /**
     * Configure la reconnaissance des graphiques.
     * @param value état demandé
     * @return ce builder
     */
    public InferRequestBuilder useChartRecognition(boolean value) {
        this.useChartRecognition = value;
        return this;
    }

    /**
     * Configure la reconnaissance des sceaux.
     * @param value état demandé
     * @return ce builder
     */
    public InferRequestBuilder useSealRecognition(boolean value) {
        this.useSealRecognition = value;
        return this;
    }

    /**
     * Configure l'OCR des blocs image.
     * @param value état demandé
     * @return ce builder
     */
    public InferRequestBuilder useOcrForImageBlock(boolean value) {
        this.useOcrForImageBlock = value;
        return this;
    }

    /**
     * Définit un seuil de layout commun.
     * @param value seuil transmis au serveur
     * @return ce builder
     */
    public InferRequestBuilder layoutThreshold(double value) {
        this.layoutThreshold = new JsonPrimitive(value);
        return this;
    }

    /**
     * Définit une configuration JSON libre pour les seuils de layout.
     * @param value valeur JSON, copiée profondément
     * @return ce builder
     */
    public InferRequestBuilder layoutThreshold(JsonElement value) {
        this.layoutThreshold = copy(value);
        return this;
    }

    /**
     * Définit les seuils de layout par catégorie.
     * @param values valeurs indexées par catégorie
     * @return ce builder
     */
    public InferRequestBuilder layoutThreshold(Map<String, ?> values) {
        this.layoutThreshold = GSON.toJsonTree(Objects.requireNonNull(values, "values ne peut pas être null"));
        return this;
    }

    /**
     * Configure le NMS de mise en page.
     * @param value état demandé
     * @return ce builder
     */
    public InferRequestBuilder layoutNms(boolean value) {
        this.layoutNms = value;
        return this;
    }

    /**
     * Définit un ratio de déclippage commun.
     * @param value ratio transmis au serveur
     * @return ce builder
     */
    public InferRequestBuilder layoutUnclipRatio(double value) {
        this.layoutUnclipRatio = new JsonPrimitive(value);
        return this;
    }

    /**
     * Définit les ratios de déclippage horizontal et vertical.
     * @param horizontal ratio horizontal
     * @param vertical ratio vertical
     * @return ce builder
     */
    public InferRequestBuilder layoutUnclipRatio(double horizontal, double vertical) {
        JsonArray values = new JsonArray(2);
        values.add(horizontal);
        values.add(vertical);
        this.layoutUnclipRatio = values;
        return this;
    }

    /**
     * Définit une configuration JSON libre des ratios de déclippage.
     * @param value valeur JSON, copiée profondément
     * @return ce builder
     */
    public InferRequestBuilder layoutUnclipRatio(JsonElement value) {
        this.layoutUnclipRatio = copy(value);
        return this;
    }

    /**
     * Définit les ratios de déclippage par catégorie.
     * @param values valeurs indexées par catégorie
     * @return ce builder
     */
    public InferRequestBuilder layoutUnclipRatio(Map<String, ?> values) {
        this.layoutUnclipRatio = GSON.toJsonTree(Objects.requireNonNull(values, "values ne peut pas être null"));
        return this;
    }

    /**
     * Définit un mode commun de fusion des boîtes.
     * @param value nom du mode
     * @return ce builder
     */
    public InferRequestBuilder layoutMergeBboxesMode(String value) {
        this.layoutMergeBboxesMode = new JsonPrimitive(requireText(value, "layoutMergeBboxesMode"));
        return this;
    }

    /**
     * Définit une configuration JSON libre des modes de fusion.
     * @param value valeur JSON, copiée profondément
     * @return ce builder
     */
    public InferRequestBuilder layoutMergeBboxesMode(JsonElement value) {
        this.layoutMergeBboxesMode = copy(value);
        return this;
    }

    /**
     * Définit les modes de fusion des boîtes par catégorie.
     * @param values valeurs indexées par catégorie
     * @return ce builder
     */
    public InferRequestBuilder layoutMergeBboxesMode(Map<String, ?> values) {
        this.layoutMergeBboxesMode = GSON.toJsonTree(Objects.requireNonNull(values, "values ne peut pas être null"));
        return this;
    }

    /**
     * Définit une forme de zone connue.
     * @param value forme demandée
     * @return ce builder
     */
    public InferRequestBuilder layoutShapeMode(LayoutShapeMode value) {
        return layoutShapeMode(Objects.requireNonNull(value, "layoutShapeMode ne peut pas être null").value());
    }

    /**
     * Définit une forme de zone brute.
     * @param value valeur transmise au serveur
     * @return ce builder
     */
    public InferRequestBuilder layoutShapeMode(String value) {
        this.layoutShapeMode = requireText(value, "layoutShapeMode");
        return this;
    }

    /**
     * Définit le label ou l'instruction du modèle.
     * @param value texte transmis au serveur
     * @return ce builder
     */
    public InferRequestBuilder promptLabel(String value) {
        this.promptLabel = value;
        return this;
    }

    /**
     * Configure le formatage du contenu des blocs.
     * @param value état demandé
     * @return ce builder
     */
    public InferRequestBuilder formatBlockContent(boolean value) {
        this.formatBlockContent = value;
        return this;
    }

    /**
     * Définit la pénalité de répétition du modèle.
     * @param value pénalité transmise au serveur
     * @return ce builder
     */
    public InferRequestBuilder repetitionPenalty(double value) {
        this.repetitionPenalty = value;
        return this;
    }

    /**
     * Définit la température d'échantillonnage.
     * @param value température transmise au serveur
     * @return ce builder
     */
    public InferRequestBuilder temperature(double value) {
        this.temperature = value;
        return this;
    }

    /**
     * Définit le seuil d'échantillonnage top-p.
     * @param value seuil transmis au serveur
     * @return ce builder
     */
    public InferRequestBuilder topP(double value) {
        this.topP = value;
        return this;
    }

    /**
     * Définit le nombre minimal de pixels.
     * @param value entier strictement positif
     * @return ce builder
     */
    public InferRequestBuilder minPixels(int value) {
        this.minPixels = value;
        return this;
    }

    /**
     * Définit le nombre maximal de pixels.
     * @param value entier strictement positif
     * @return ce builder
     */
    public InferRequestBuilder maxPixels(int value) {
        this.maxPixels = value;
        return this;
    }

    /**
     * Définit le nombre maximal de nouveaux tokens.
     * @param value entier strictement positif
     * @return ce builder
     */
    public InferRequestBuilder maxNewTokens(int value) {
        this.maxNewTokens = value;
        return this;
    }

    /**
     * Configure la fusion des blocs de mise en page.
     * @param value état demandé
     * @return ce builder
     */
    public InferRequestBuilder mergeLayoutBlocks(boolean value) {
        this.mergeLayoutBlocks = value;
        return this;
    }

    /**
     * Ajoute un label à exclure du Markdown.
     * @param value label à ignorer
     * @return ce builder
     */
    public InferRequestBuilder markdownIgnoreLabel(String value) {
        markdownIgnoreLabels.add(requireText(value, "markdownIgnoreLabel"));
        return this;
    }

    /**
     * Ajoute plusieurs labels à exclure du Markdown.
     * @param values labels à ignorer
     * @return ce builder
     */
    public InferRequestBuilder markdownIgnoreLabels(String... values) {
        Objects.requireNonNull(values, "markdownIgnoreLabels ne peut pas être null");
        Arrays.stream(values).forEach(this::markdownIgnoreLabel);
        return this;
    }

    /**
     * Remplace les arguments supplémentaires du modèle VLM.
     * @param values arguments indexés par leur nom
     * @return ce builder
     */
    public InferRequestBuilder vlmExtraArgs(Map<String, ?> values) {
        Objects.requireNonNull(values, "vlmExtraArgs ne peut pas être null");
        this.vlmExtraArgs = new LinkedHashMap<>(values);
        return this;
    }

    /**
     * Ajoute ou remplace un argument supplémentaire du modèle VLM.
     * @param name nom de l'argument
     * @param value valeur de l'argument
     * @return ce builder
     */
    public InferRequestBuilder vlmExtraArg(String name, Object value) {
        if (vlmExtraArgs == null) {
            vlmExtraArgs = new LinkedHashMap<>();
        }
        vlmExtraArgs.put(requireText(name, "name"), value);
        return this;
    }

    /**
     * Configure l'embellissement du Markdown.
     * @param value état demandé
     * @return ce builder
     */
    public InferRequestBuilder prettifyMarkdown(boolean value) {
        this.prettifyMarkdown = value;
        return this;
    }

    /**
     * Configure l'affichage des numéros de formules.
     * @param value état demandé
     * @return ce builder
     */
    public InferRequestBuilder showFormulaNumber(boolean value) {
        this.showFormulaNumber = value;
        return this;
    }

    /**
     * Configure le retour des images Markdown.
     * @param value état demandé
     * @return ce builder
     */
    public InferRequestBuilder returnMarkdownImages(boolean value) {
        this.returnMarkdownImages = value;
        return this;
    }

    /**
     * Configure la restructuration pendant l'analyse.
     * @param value état demandé
     * @return ce builder
     */
    public InferRequestBuilder restructurePages(boolean value) {
        this.restructurePages = value;
        return this;
    }

    /**
     * Configure la fusion des tableaux.
     * @param value état demandé
     * @return ce builder
     */
    public InferRequestBuilder mergeTables(boolean value) {
        this.mergeTables = value;
        return this;
    }

    /**
     * Configure le recalcul des niveaux de titres.
     * @param value état demandé
     * @return ce builder
     */
    public InferRequestBuilder relevelTitles(boolean value) {
        this.relevelTitles = value;
        return this;
    }

    /**
     * Configure les sorties de visualisation.
     * @param value état demandé
     * @return ce builder
     */
    public InferRequestBuilder visualize(boolean value) {
        this.visualize = value;
        return this;
    }

    /**
     * Définit l'identifiant de corrélation.
     * @param value identifiant transmis au serveur
     * @return ce builder
     */
    public InferRequestBuilder logId(String value) {
        this.logId = value;
        return this;
    }

    /**
     * Construit une requête indépendante de l'état mutable du builder.
     *
     * @return nouvelle requête d'analyse validée
     */
    public InferRequest build() {
        return new InferRequest(
                file,
                fileType,
                outputFormats.isEmpty() ? null : List.copyOf(outputFormats),
                useDocOrientationClassify,
                useDocUnwarping,
                useLayoutDetection,
                useChartRecognition,
                useSealRecognition,
                useOcrForImageBlock,
                copy(layoutThreshold),
                layoutNms,
                copy(layoutUnclipRatio),
                copy(layoutMergeBboxesMode),
                layoutShapeMode,
                promptLabel,
                formatBlockContent,
                repetitionPenalty,
                temperature,
                topP,
                minPixels,
                maxPixels,
                maxNewTokens,
                mergeLayoutBlocks,
                markdownIgnoreLabels.isEmpty() ? null : List.copyOf(markdownIgnoreLabels),
                vlmExtraArgs == null ? null : new LinkedHashMap<>(vlmExtraArgs),
                prettifyMarkdown,
                showFormulaNumber,
                returnMarkdownImages,
                restructurePages,
                mergeTables,
                relevelTitles,
                visualize,
                logId
        );
    }

    /**
     * Copie profondément un élément JSON lorsqu'il est présent.
     * @param value élément à copier
     * @return copie profonde, ou {@code null}
     */
    private static JsonElement copy(JsonElement value) {
        return value == null ? null : value.deepCopy();
    }

    /**
     * Valide qu'une chaîne est renseignée.
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
