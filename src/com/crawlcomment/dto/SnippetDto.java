package com.crawlcomment.dto;

import java.util.HashMap;

public class SnippetDto {
    private boolean canReply;
    private boolean isPublic;
    private HashMap<String,Object> topLevelComment;
    private Long totalReplyCount;
    private String videoId;

    public boolean isCanReply() {
        return canReply;
    }

    public void setCanReply(boolean canReply) {
        this.canReply = canReply;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public HashMap<String, Object> getTopLevelComment() {
        return topLevelComment;
    }

    public void setTopLevelComment(HashMap<String, Object> topLevelComment) {
        this.topLevelComment = topLevelComment;
    }

    public Long getTotalReplyCount() {
        return totalReplyCount;
    }

    public void setTotalReplyCount(Long totalReplyCount) {
        this.totalReplyCount = totalReplyCount;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
