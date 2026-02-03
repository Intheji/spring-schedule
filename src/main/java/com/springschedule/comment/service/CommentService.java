package com.springschedule.comment.service;

import com.springschedule.comment.dto.CreateCommentRequest;
import com.springschedule.comment.dto.CreateCommentResponse;
import com.springschedule.comment.entity.Comment;
import com.springschedule.comment.repository.CommentRepository;
import com.springschedule.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public CreateCommentResponse save(Long scheduleId, CreateCommentRequest request) {
        boolean existence = scheduleRepository.existsById(scheduleId);
        if (!existence) {
            throw new IllegalStateException("일정이 없는데용?");
        }

        long count = commentRepository.countByScheduleId(scheduleId);
        if (count >= 10) {
            throw new IllegalStateException("댓글은 일정 하나당 최대 10개까지 작성할 수 있어요. 지금 10개 다 씀");
        }

        Comment comment = new Comment(
                scheduleId,
                request.getContent(),
                request.getAuthorName(),
                request.getPassword()
        );
        Comment savedComment = commentRepository.save(comment);

        return new CreateCommentResponse(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getAuthorName(),
                savedComment.getScheduleId(),
                savedComment.getCreatedAt(),
                savedComment.getModifiedAt()
        );
    }
}
