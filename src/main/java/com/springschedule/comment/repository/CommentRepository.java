package com.springschedule.comment.repository;

import com.springschedule.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    long countByScheduleId(Long scheduleId);
}
