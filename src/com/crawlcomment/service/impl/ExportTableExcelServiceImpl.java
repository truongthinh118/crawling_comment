package com.crawlcomment.service.impl;

import com.crawlcomment.dto.CommentInfoDto;
import com.crawlcomment.service.ExportTableExcelService;
import com.crawlcomment.util.DateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ExportTableExcelServiceImpl implements ExportTableExcelService {
    public final int COLUMN_INDEX_TYPE = 0;
    public final int COLUMN_INDEX_AUTHOR = 1;

    public final int COLUMN_INDEX_AUTHOR_CHANNEL_URL = 2;
    public final int COLUMN_INDEX_COMMENTCONTENT = 3;
    public final int COLUMN_INDEX_TOTALREPLYCOUNT = 4;
    public final int COLUMN_INDEX_TOTALLIKECOUNT = 5;
    public final int COLUMN_INDEX_PUBLISHEDDATE = 6;
    public final int COLUMN_INDEX_UPDATEDDATE = 7;
    private CellStyle cellStyleFormatNumber = null;

    public void writeExcel(List<CommentInfoDto> list, String excelFilePath, String sheetname) throws IOException {
        // Create Workbook
        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);

        // Create sheet
        SXSSFSheet sheet = workbook.createSheet(sheetname); // Create sheet with sheet name

        // register the columns you wish to track and compute the column width
        sheet.trackAllColumnsForAutoSizing();

        int rowIndex = 0;

        // Write header
        writeHeader(sheet, rowIndex);

        // Write data
        rowIndex++;
        for (CommentInfoDto dto : list) {
            // Create row
            SXSSFRow row = sheet.createRow(rowIndex);
            // Write data on row
            writeBook(dto, row);
            rowIndex++;
        }

        // Auto resize column witdth
        int numberOfColumn = sheet.getRow(0).getPhysicalNumberOfCells();
        autosizeColumn(sheet, numberOfColumn);

        // Create file excel
        createOutputFile(workbook, excelFilePath);
        System.out.println("Done!!!");
    }

    private void createOutputFile(SXSSFWorkbook workbook, String excelFilePath) throws IOException {
        try (OutputStream os = new FileOutputStream(excelFilePath)) {
            workbook.write(os);
        }
    }

    private void autosizeColumn(SXSSFSheet sheet, int lastColumn) {
        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
    }


    private void writeBook(CommentInfoDto dto, SXSSFRow row) {
        if (cellStyleFormatNumber == null) {
            // Format number
            short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");
            // DataFormat df = workbook.createDataFormat();
            // short format = df.getFormat("#,##0");

            // Create CellStyle
            SXSSFWorkbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }

        SXSSFCell cell = row.createCell(COLUMN_INDEX_TYPE);
        cell.setCellValue(dto.getType());

        cell = row.createCell(COLUMN_INDEX_AUTHOR);
        cell.setCellValue(dto.getAuthor());

        cell = row.createCell(COLUMN_INDEX_AUTHOR_CHANNEL_URL);
        cell.setCellValue(dto.getAuthorChannelUrl());

        cell = row.createCell(COLUMN_INDEX_COMMENTCONTENT);
        cell.setCellValue(dto.getCommentContent());

        cell = row.createCell(COLUMN_INDEX_TOTALREPLYCOUNT);
        cell.setCellValue(dto.getTotalReplyCount());
        cell.setCellStyle(cellStyleFormatNumber);

        cell = row.createCell(COLUMN_INDEX_TOTALLIKECOUNT);
        cell.setCellValue(dto.getTotalLikeCount());
        cell.setCellStyle(cellStyleFormatNumber);

        cell = row.createCell(COLUMN_INDEX_PUBLISHEDDATE);
        cell.setCellValue(DateUtil.parseString(dto.getPublishedDate(), "yyyy-MM-dd HH:mm:ss"));

        cell = row.createCell(COLUMN_INDEX_UPDATEDDATE);
        cell.setCellValue(DateUtil.parseString(dto.getUpdatedAt(), "yyyy-MM-dd HH:mm:ss"));
    }

    private void writeHeader(SXSSFSheet sheet, int rowIndex) {
        // create CellStyle
        CellStyle cellStyle = createStyleForHeader(sheet);

        // Create row
        SXSSFRow row = sheet.createRow(rowIndex);

        // Create cells
        SXSSFCell cell = row.createCell(COLUMN_INDEX_TYPE);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Type");

        cell = row.createCell(COLUMN_INDEX_AUTHOR);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Author");

        cell = row.createCell(COLUMN_INDEX_AUTHOR_CHANNEL_URL);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Author Channel URL");

        cell = row.createCell(COLUMN_INDEX_COMMENTCONTENT);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Comment Content");

        cell = row.createCell(COLUMN_INDEX_TOTALREPLYCOUNT);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Total Reply Count");

        cell = row.createCell(COLUMN_INDEX_TOTALLIKECOUNT);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Like Count");

        cell = row.createCell(COLUMN_INDEX_PUBLISHEDDATE);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Published Date");

        cell = row.createCell(COLUMN_INDEX_UPDATEDDATE);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Updated Date");
    }

    private CellStyle createStyleForHeader(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14); // font size
        font.setColor(IndexedColors.WHITE.getIndex()); // text color

        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        return cellStyle;
    }
}
