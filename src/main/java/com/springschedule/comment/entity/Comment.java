package com.springschedule.comment.entity;

import com.springschedule.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long scheduleId;

    @Column(nullable = false, length = 100)
    private String content;

    @Column(nullable = false)
    private String authorName;

    @Column(nullable = false)
    private String password;

    public Comment(Long scheduleId, String content, String authorName, String password) {
        this.scheduleId = scheduleId;
        this.content = content;
        this.authorName = authorName;
        this.password = password;
    }
}
