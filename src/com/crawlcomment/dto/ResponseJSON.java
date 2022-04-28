package com.crawlcomment.dto;

import java.util.HashMap;
import java.util.List;

public class ResponseJSON {
    private String etag;
    private List<HashMap<String, Object>> items;
    private String kind;
    private String nextPageToken;
    private HashMap<String, Object> pageInfo;


    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public List<HashMap<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<HashMap<String, Object>> items) {
        this.items = items;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public HashMap<String, Object> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(HashMap<String, Object> pageInfo) {
        this.pageInfo = pageInfo;
    }
}
