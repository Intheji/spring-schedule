package com.springschedule.comment.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateCommentResponse {

    private final Long id;
    private final String content;
    private final String authorName;
    private final Long schedulId;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public CreateCommentResponse(Long id, String content, String authorName, Long schedulId, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.content = content;
        this.authorName = authorName;
        this.schedulId = schedulId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
