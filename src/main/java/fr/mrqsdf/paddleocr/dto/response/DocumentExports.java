package fr.mrqsdf.paddleocr.dto.response;

/**
 * Ensemble des documents exportés par PaddleOCR.
 *
 * @param docx export Microsoft Word éventuel
 */
public record DocumentExports(
        ExportContent docx
) {}
