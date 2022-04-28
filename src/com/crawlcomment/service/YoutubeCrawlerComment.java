package com.crawlcomment.service;

import com.crawlcomment.dto.CommentInfoDto;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface YoutubeCrawlerComment {
    List<CommentInfoDto> crawlCommentYoutube(String videoId) throws IOException, GeneralSecurityException;
}
