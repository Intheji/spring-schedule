package com.springschedule.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCommentRequest {

    private String content;
    private String authorName;
    private String password;
}
