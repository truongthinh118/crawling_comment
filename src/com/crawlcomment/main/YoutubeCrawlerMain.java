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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

public class YoutubeCrawlerMain {

    public static void main(String[] args)
            throws GeneralSecurityException, IOException, GoogleJsonResponseException {

        Scanner sc = new Scanner(System.in);

        StopWatch crawlTime = new StopWatch();
        StopWatch exportTime = new StopWatch();

//        Get Comment from Youtube By Call Google Api

        System.out.print("Video ID: ");
        String videoID = sc.next();
        crawlTime.start();
        YoutubeCrawlerComment youtubeCrawler = new YoutubeCrawlerCommentImpl();
        List<CommentInfoDto> commentList = youtubeCrawler.crawlCommentYoutube(videoID);
        System.out.println("Crawl Time: " + crawlTime.getTime());

//        Export DataList to Excel File
        System.out.print("Path to export: ");
        String pathToExport = sc.next();

        exportTime.start();
        ExportTableExcelService service = new ExportTableExcelServiceImpl();
        service.writeExcel(commentList, pathToExport, "Crawl Result");
        System.out.println("Export Time: " + exportTime.getTime());

    }
}