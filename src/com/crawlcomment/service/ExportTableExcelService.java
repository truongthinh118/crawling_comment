package com.crawlcomment.service;

import com.crawlcomment.dto.CommentInfoDto;

import java.io.IOException;
import java.util.List;

public interface ExportTableExcelService {
    void writeExcel(List<CommentInfoDto> list, String excelFilePath, String sheetname) throws IOException;
}
