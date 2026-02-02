package com.springschedule.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleCreateRequest {

    private String title;
    private String content;
    private String authorName;
    private String password;
}
