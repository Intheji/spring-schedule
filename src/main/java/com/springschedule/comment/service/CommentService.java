package com.springschedule.comment.service;

import com.springschedule.comment.dto.CreateCommentRequest;
import com.springschedule.comment.dto.CreateCommentResponse;
import com.springschedule.comment.entity.Comment;
import com.springschedule.comment.repository.CommentRepository;
import com.springschedule.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.springschedule.common.validation.InputValidator.requireMaxLength;
import static com.springschedule.common.validation.InputValidator.requireText;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    // 댓글 생성
    @Transactional
    public CreateCommentResponse save(Long scheduleId, CreateCommentRequest request) {
        // 존재하지 않는 일정에는 댓글을 달 수 없다 존재 여부만 확인
        boolean existence = scheduleRepository.existsById(scheduleId);
        if (!existence) {
            throw new IllegalStateException("일정이 없는데용?");
        }

        // 댓글 일정 1개당 10개까지만 허용
        long count = commentRepository.countByScheduleId(scheduleId);
        if (count >= 10) {
            throw new IllegalArgumentException("댓글은 일정 하나당 최대 10개까지 작성할 수 있어요. 지금 10개 다 씀");
        }

        requireText(request.getContent(), "comment 내용");
        requireMaxLength(request.getContent(), "comment 내용", 100);
        requireText(request.getAuthorName(), "작성자이름");
        requireText(request.getPassword(), "비밀번호");

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
                savedComment.getCreatedAt(),
                savedComment.getModifiedAt()
        );
    }
}
