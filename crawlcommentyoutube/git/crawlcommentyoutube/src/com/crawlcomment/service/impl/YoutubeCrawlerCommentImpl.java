package com.crawlcomment.service.impl;

import com.crawlcomment.dto.CommentInfoDto;
import com.crawlcomment.dto.ResponseJSON;
import com.crawlcomment.dto.SnippetCommentDto;
import com.crawlcomment.dto.SnippetDto;
import com.crawlcomment.main.YoutubeCrawlerMain;
import com.crawlcomment.service.YoutubeCrawlerComment;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.*;

public class YoutubeCrawlerCommentImpl implements YoutubeCrawlerComment {
    private Gson gson = new Gson();

    private String VIDEO_ID;
    private final String CLIENT_SECRETS = "client_secret_525499905849-tipq75cbuvlcv5r4t6misd6lrdlclb5r.apps.googleusercontent.com.json";
    private final Collection<String> SCOPES =
            Arrays.asList("https://www.googleapis.com/auth/youtube.force-ssl");

    private final String APPLICATION_NAME = "Crawl Comment Youtube";
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Create an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public Credential authorize(final NetHttpTransport httpTransport) throws IOException {
        // Load client secrets.
        InputStream in = YoutubeCrawlerMain.class.getResourceAsStream(CLIENT_SECRETS);
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .build();
        Credential credential =
                new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        return credential;
    }

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    public YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(httpTransport);
        return new YouTube.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public List<CommentInfoDto> crawlCommentYoutube(String videoId) throws IOException, GeneralSecurityException {
        this.VIDEO_ID = videoId;
        YouTube youtubeService = getService();
        // Define and execute the API request
        List<String> list = new ArrayList<String>();
        list.add("snippet");
        list.add("replies");
        YouTube.CommentThreads.List request = youtubeService.commentThreads()
                .list(list);

        List<CommentInfoDto> result = new ArrayList<>();

        CommentThreadListResponse response = request.setMaxResults(1L).setVideoId(VIDEO_ID).execute();

        ResponseJSON responseJSON = gson.fromJson(String.valueOf(response), new TypeToken<ResponseJSON>() {
        }.getType());
        result = processResponse(responseJSON, result);
        result = crawlNextPage(request, responseJSON.getNextPageToken(), result);

        return result;
    }

    private List<CommentInfoDto> crawlNextPage(YouTube.CommentThreads.List request, String nextPageToken, List<CommentInfoDto> result) throws IOException {
        while (true) {
            CommentThreadListResponse response = request.setMaxResults(100L).setPageToken(nextPageToken).setVideoId(VIDEO_ID).execute();

            ResponseJSON responseJSON = gson.fromJson(String.valueOf(response), new TypeToken<ResponseJSON>() {
            }.getType());

            result = processResponse(responseJSON, result);

            String newNextPageToken = responseJSON.getNextPageToken();

            if (newNextPageToken == null) {
                break;
            } else nextPageToken = newNextPageToken;
        }
        return result;
    }

    private List<CommentInfoDto> processResponse(ResponseJSON responseJSON, List<CommentInfoDto> result) {
        for (HashMap<String, Object> item : responseJSON.getItems()) {
            CommentInfoDto dto = new CommentInfoDto();
            String snippetString = gson.toJson(item.get("snippet"));

            SnippetDto snippetdto = gson.fromJson(snippetString, new TypeToken<SnippetDto>() {
            }.getType());

            dto.setTotalReplyCount(snippetdto.getTotalReplyCount());

            Map<String, Object> topLevelComment = snippetdto.getTopLevelComment();

            String secondSnippetString = gson.toJson(topLevelComment.get("snippet"));

            SnippetCommentDto snippetCommentDto = gson.fromJson(secondSnippetString, new TypeToken<SnippetCommentDto>() {
            }.getType());

            dto.setType("MAIN_COMMENT");
            dto.setAuthor(snippetCommentDto.getAuthorDisplayName());
            dto.setAuthorChannelUrl(snippetCommentDto.getAuthorChannelUrl());
            dto.setTotalLikeCount(snippetCommentDto.getLikeCount());
            dto.setPublishedDate(snippetCommentDto.getPublishedAt());
            dto.setCommentContent(snippetCommentDto.getTextOriginal());
            dto.setUpdatedAt(snippetCommentDto.getUpdatedAt());
            result.add(dto);
//            System.out.println(gson.toJson(snippetCommentDto.getTextOriginal()));

            String repliesString = gson.toJson(item.getOrDefault("replies", null));
            if (repliesString != null) {
                try {

                    HashMap<String, Object> repliesMap = gson.fromJson(repliesString, new TypeToken<HashMap<String, Object>>() {
                    }.getType());

                    String replies = gson.toJson(repliesMap.get("comments"));

                    List<HashMap<String, Object>> replyContentList = gson.fromJson(replies, new TypeToken<List<HashMap<String, Object>>>() {
                    }.getType());

                    for (HashMap<String, Object> reply : replyContentList) {
                        SnippetCommentDto replydto = gson.fromJson(gson.toJson(reply.get("snippet")), new TypeToken<SnippetCommentDto>() {
                        }.getType());

                        CommentInfoDto replyCommentDto = new CommentInfoDto();
                        replyCommentDto.setType("COMMENT_REPLY");
                        replyCommentDto.setTotalReplyCount(0L);
                        replyCommentDto.setAuthor(replydto.getAuthorDisplayName());
                        replyCommentDto.setAuthorChannelUrl(replydto.getAuthorChannelUrl());
                        replyCommentDto.setTotalLikeCount(replydto.getLikeCount());
                        replyCommentDto.setPublishedDate(replydto.getPublishedAt());
                        replyCommentDto.setUpdatedAt(replydto.getUpdatedAt());
                        replyCommentDto.setCommentContent(replydto.getTextOriginal());
                        result.add(replyCommentDto);
                    }

                } catch (Exception e) {

                }
            }
        }
        return result;
    }
}
