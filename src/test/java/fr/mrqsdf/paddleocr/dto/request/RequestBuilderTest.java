package fr.mrqsdf.paddleocr.dto.request;

import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class RequestBuilderTest {

    @Test
    void buildsACompleteInferRequestWithTypedValues() {
        InferRequest request = InferRequest.builder("base64-content")
                .fileType(InputFileType.PDF)
                .outputFormat(OutputFormat.DOCX)
                .useDocOrientationClassify(true)
                .useDocUnwarping(false)
                .useLayoutDetection(true)
                .useChartRecognition(true)
                .useSealRecognition(false)
                .useOcrForImageBlock(true)
                .layoutThreshold(0.7)
                .layoutNms(true)
                .layoutUnclipRatio(1.2, 1.3)
                .layoutMergeBboxesMode("large")
                .layoutShapeMode(LayoutShapeMode.POLY)
                .promptLabel("ocr")
                .formatBlockContent(true)
                .repetitionPenalty(1.1)
                .temperature(0.2)
                .topP(0.9)
                .minPixels(100)
                .maxPixels(1_000_000)
                .maxNewTokens(2048)
                .mergeLayoutBlocks(true)
                .markdownIgnoreLabels("header", "footer")
                .vlmExtraArg("seed", 42)
                .prettifyMarkdown(true)
                .showFormulaNumber(true)
                .returnMarkdownImages(true)
                .restructurePages(true)
                .mergeTables(true)
                .relevelTitles(true)
                .visualize(false)
                .logId("request-1")
                .build();

        assertEquals(0, request.fileType());
        assertEquals("docx", request.outputFormats().getFirst());
        assertEquals("poly", request.layoutShapeMode());
        assertEquals(2, request.layoutUnclipRatio().getAsJsonArray().size());
        assertEquals(42, request.vlmExtraArgs().get("seed"));
        assertFalse(request.visualize());
    }

    @Test
    void acceptsFutureRawValuesAndOmitsUnsetCollections() {
        InferRequest request = InferRequest.builder("content")
                .fileType(2)
                .outputFormat("future-format")
                .layoutShapeMode("future-shape")
                .build();

        assertEquals("future-format", request.outputFormats().getFirst());
        assertEquals("future-shape", request.layoutShapeMode());
        assertEquals(2, request.fileType());
        assertNull(request.markdownIgnoreLabels());
    }

    @Test
    void buildsPageAndRestructureRequest() {
        Page page = Page.builder(JsonParser.parseString("{\"block\":1}").getAsJsonObject())
                .markdownImage("image.png", "base64-image")
                .build();

        RestructurePagesRequest request = RestructurePagesRequest.builder()
                .page(page)
                .outputFormats(OutputFormat.DOCX)
                .mergeTables(true)
                .relevelTitles(false)
                .concatenatePages(true)
                .prettifyMarkdown(true)
                .showFormulaNumber(false)
                .returnMarkdownImages(true)
                .logId("request-2")
                .build();

        assertEquals(1, request.pages().size());
        assertEquals(Map.of("image.png", "base64-image"), request.pages().getFirst().markdownImages());
        assertEquals("docx", request.outputFormats().getFirst());
    }
}
