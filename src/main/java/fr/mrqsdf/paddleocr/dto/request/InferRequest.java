package fr.mrqsdf.paddleocr.dto.request;

import com.google.gson.JsonElement;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Requête d'analyse d'un document par l'endpoint {@code /layout-parsing}.
 * Les propriétés facultatives laissées à {@code null} sont omises du JSON.
 *
 * @param file contenu ou référence du document, selon les formats acceptés par le serveur
 * @param fileType type numérique du fichier
 * @param outputFormats formats d'export supplémentaires demandés
 * @param useDocOrientationClassify activation de la classification d'orientation
 * @param useDocUnwarping activation du redressement du document
 * @param useLayoutDetection activation de la détection de mise en page
 * @param useChartRecognition activation de la reconnaissance des graphiques
 * @param useSealRecognition activation de la reconnaissance des sceaux
 * @param useOcrForImageBlock activation de l'OCR dans les blocs image
 * @param layoutThreshold seuil commun ou seuils par catégorie
 * @param layoutNms activation du NMS de mise en page
 * @param layoutUnclipRatio ratio commun, paire de ratios ou ratios par catégorie
 * @param layoutMergeBboxesMode mode commun ou modes de fusion par catégorie
 * @param layoutShapeMode forme des zones de mise en page
 * @param promptLabel label ou instruction transmis au modèle
 * @param formatBlockContent activation du formatage du contenu des blocs
 * @param repetitionPenalty pénalité de répétition du modèle
 * @param temperature température d'échantillonnage
 * @param topP seuil d'échantillonnage top-p
 * @param minPixels nombre minimal de pixels, strictement positif
 * @param maxPixels nombre maximal de pixels, strictement positif
 * @param maxNewTokens nombre maximal de nouveaux tokens, strictement positif
 * @param mergeLayoutBlocks activation de la fusion des blocs de mise en page
 * @param markdownIgnoreLabels labels exclus du Markdown
 * @param vlmExtraArgs arguments supplémentaires transmis au modèle VLM
 * @param prettifyMarkdown activation de l'embellissement du Markdown
 * @param showFormulaNumber conservation des numéros de formules
 * @param returnMarkdownImages inclusion des images Markdown dans la réponse
 * @param restructurePages activation de la restructuration pendant l'analyse
 * @param mergeTables activation de la fusion des tableaux
 * @param relevelTitles activation du recalcul des niveaux de titres
 * @param visualize activation des sorties de visualisation
 * @param logId identifiant de corrélation facultatif
 */
public record InferRequest(
        String file,
        Integer fileType,
        List<String> outputFormats,

        Boolean useDocOrientationClassify,
        Boolean useDocUnwarping,
        Boolean useLayoutDetection,
        Boolean useChartRecognition,
        Boolean useSealRecognition,
        Boolean useOcrForImageBlock,

        JsonElement layoutThreshold,
        Boolean layoutNms,
        JsonElement layoutUnclipRatio,
        JsonElement layoutMergeBboxesMode,
        String layoutShapeMode,

        String promptLabel,
        Boolean formatBlockContent,

        Double repetitionPenalty,
        Double temperature,
        Double topP,

        Integer minPixels,
        Integer maxPixels,
        Integer maxNewTokens,

        Boolean mergeLayoutBlocks,
        List<String> markdownIgnoreLabels,
        Map<String, Object> vlmExtraArgs,

        Boolean prettifyMarkdown,
        Boolean showFormulaNumber,
        Boolean returnMarkdownImages,
        Boolean restructurePages,
        Boolean mergeTables,
        Boolean relevelTitles,

        Boolean visualize,
        String logId
) {

    /**
     * Valide les valeurs contraintes et effectue des copies défensives.
     *
     * @throws IllegalArgumentException si le fichier est vide ou si une limite numérique
     *                                  contrainte n'est pas strictement positive
     */
    public InferRequest {
        if (file == null || file.isBlank()) {
            throw new IllegalArgumentException(
                    "Le champ file est obligatoire"
            );
        }

        if (minPixels != null && minPixels <= 0) {
            throw new IllegalArgumentException(
                    "minPixels doit être supérieur à 0"
            );
        }

        if (maxPixels != null && maxPixels <= 0) {
            throw new IllegalArgumentException(
                    "maxPixels doit être supérieur à 0"
            );
        }

        if (maxNewTokens != null && maxNewTokens <= 0) {
            throw new IllegalArgumentException(
                    "maxNewTokens doit être supérieur à 0"
            );
        }

        outputFormats = outputFormats == null ? null : List.copyOf(outputFormats);
        layoutThreshold = copy(layoutThreshold);
        layoutUnclipRatio = copy(layoutUnclipRatio);
        layoutMergeBboxesMode = copy(layoutMergeBboxesMode);
        markdownIgnoreLabels = markdownIgnoreLabels == null
                ? null
                : List.copyOf(markdownIgnoreLabels);
        vlmExtraArgs = vlmExtraArgs == null
                ? null
                : Collections.unmodifiableMap(new LinkedHashMap<>(vlmExtraArgs));
    }

    /**
     * Crée un builder avec le document obligatoire.
     *
     * @param file contenu ou référence du document
     * @return nouveau builder d'analyse
     */
    public static InferRequestBuilder builder(String file) {
        return new InferRequestBuilder(file);
    }

    /**
     * Copie un élément JSON mutable lorsqu'il est présent.
     *
     * @param value élément à copier
     * @return copie profonde, ou {@code null}
     */
    private static JsonElement copy(JsonElement value) {
        return value == null ? null : value.deepCopy();
    }
}
