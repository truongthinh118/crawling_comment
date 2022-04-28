package com.crawlcomment.main;
/**
 * Sample Java code for youtube.commentThreads.list
 * See instructions for running these code samples locally:
 * https://developers.google.com/explorer-help/code-samples#java
 */

import com.crawlcomment.dto.CommentInfoDto;
import com.crawlcomment.service.ExportTableExcelService;
import com.crawlcomment.service.YoutubeCrawlerComment;
import com.crawlcomment.service.impl.ExportTableExcelServiceImpl;
import com.crawlcomment.service.impl.YoutubeCrawlerCommentImpl;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;

import org.apache.commons.lang.time.StopWatch;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

public class YoutubeCrawlerMain {
    public static void main(String[] args)
            throws GeneralSecurityException, IOException, GoogleJsonResponseException {
        StopWatch w = new StopWatch();
        w.start();
//        Get Comment from Youtube By Call Google Api
        YoutubeCrawlerComment youtubeCrawler = new YoutubeCrawlerCommentImpl();
        List<CommentInfoDto> commentList = youtubeCrawler.crawlCommentYoutube("F8jsrN7h0-c");
//        Export DataList to Excel File
        ExportTableExcelService service = new ExportTableExcelServiceImpl();
        service.writeExcel(commentList, "/home/truongthinh/Projects/CHUYỆN XÓM TUI PHẦN 2.xlsx",
                "FULL 3 TẬP");
        System.out.println("Total times " + w.getTime());
    }
}