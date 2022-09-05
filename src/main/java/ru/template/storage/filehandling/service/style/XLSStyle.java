package ru.template.storage.filehandling.service.style;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;


public abstract class XLSStyle {

    public static final int ROW_OFFSET = 2;

    public static final String NAME_ID_COLUMN = "№ п.п.";

    public static final int ALL_COLUMN_WIDTH = 6000;
    public static final String SHEET_NAME = "Лист1";
    public static final String IF_CELL_IS_NULL = "";

    public static final List<String> IGNORE_FIELDS = new ArrayList<>(
            List.of(new String[]{
                    "id", "createdAt", "updatedAt", "createdBy", "updatedBy"
            }));

    public static CellStyle createHeaderTableCellsStyle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);

        CellStyle style = workbook.createCellStyle();
        generalSettings(style, BorderStyle.THIN);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
//        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(font);

        return style;
    }

    public static CellStyle createStandardTableCellsStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        generalSettings(style, BorderStyle.THIN);

        return style;
    }

    public static CellStyle createHeaderSheetStyle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 14);

        CellStyle style = workbook.createCellStyle();
        generalSettings(style, BorderStyle.NONE);
        style.setFont(font);

        return style;
    }

    public static CellStyle createFilterTitleStyle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);

        CellStyle style = workbook.createCellStyle();
        generalSettings(style, BorderStyle.NONE);
        style.setFont(font);

        return style;
    }

    public static CellStyle createFilterFieldStyle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);

        CellStyle style = workbook.createCellStyle();
        generalSettings(style, BorderStyle.NONE);
        style.setFont(font);

        return style;
    }

    private static void generalSettings(CellStyle style,
                                        BorderStyle borderStyle) {
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderLeft(borderStyle);
        style.setBorderRight(borderStyle);
        style.setBorderBottom(borderStyle);
        style.setBorderTop(borderStyle);
        style.setWrapText(true);

    }
}
