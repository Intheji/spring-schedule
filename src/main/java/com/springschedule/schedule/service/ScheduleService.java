package com.springschedule.schedule.service;

import com.springschedule.comment.dto.GetCommentResponse;
import com.springschedule.comment.entity.Comment;
import com.springschedule.comment.repository.CommentRepository;
import com.springschedule.schedule.dto.*;
import com.springschedule.schedule.entity.Schedule;
import com.springschedule.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.springschedule.common.validation.InputValidator.requireMaxLength;
import static com.springschedule.common.validation.InputValidator.requireText;


@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;

    // 일정 생성
    @Transactional
    public CreateScheduleResponse save(CreateScheduleRequest request) {

        // 요청 데이터가 비었는지 길이 제한을 넘는지 검증
        requireText(request.getTitle(), "schedule 제목");
        requireMaxLength(request.getTitle(), "schedule 제목", 30);
        requireText(request.getContent(),"schedule 내용");
        requireMaxLength(request.getContent(), "schedule 내용", 200);
        requireText(request.getAuthorName(), "작성자이름");
        requireText(request.getPassword(), "비밀번호");

        Schedule schedule = new Schedule(
                request.getTitle(),
                request.getContent(),
                request.getAuthorName(),
                request.getPassword()
        );

        Schedule saved = scheduleRepository.save(schedule);

        // 비밀번호를 빼고 응답
        return new CreateScheduleResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getContent(),
                saved.getAuthorName(),
                saved.getCreatedAt(),
                saved.getModifiedAt()
        );
    }

    // 일정 단건 조회
    @Transactional(readOnly = true)
    public GetScheduleAndCommentsResponse findOne(Long scheduleId) {

        // 값이 없으면 예외 발생
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("일정이 없는데용?")
        );

        // 댓글 조회 오름차순으로 가져옴
        List<Comment> comments = commentRepository.findByScheduleIdOrderByCreatedAtAsc(scheduleId);

        // 반환
        List<GetCommentResponse> commentDtos = new ArrayList<>();
        for (Comment comment : comments) {
            GetCommentResponse dto = new GetCommentResponse(
                    comment.getId(),
                    comment.getContent(),
                    comment.getAuthorName(),
                    comment.getCreatedAt(),
                    comment.getModifiedAt()
            );
            commentDtos.add(dto);
        }

        // 응답
        return new GetScheduleAndCommentsResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getAuthorName(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt(),
                commentDtos
        );
    }

    // 전체 일정 조회
    @Transactional(readOnly = true)
    public List<GetScheduleResponse> findAll(String authorName) {

        List<Schedule> schedules;

        // authorName이 공백이면 전체 조회, 아니면 작성자 조회
        if (authorName == null || authorName.isBlank()) {
            schedules = scheduleRepository.findAllByOrderByModifiedAtDesc();
        } else {
            schedules = scheduleRepository.findByAuthorNameOrderByModifiedAtDesc(authorName);
        }

        List<GetScheduleResponse> responses = new ArrayList<>();
        for (Schedule schedule : schedules) {
            GetScheduleResponse response = new GetScheduleResponse(
                    schedule.getId(),
                    schedule.getTitle(),
                    schedule.getContent(),
                    schedule.getAuthorName(),
                    schedule.getCreatedAt(),
                    schedule.getModifiedAt()
            );
            responses.add(response);
        }
        return responses;
    }

    // 일정 수정 (비밀번호 검증이 필요)
    @Transactional
    public UpdateScheduleResponse update(Long scheduleId, UpdateScheduleRequest request) {

        // 수정 요정에서 필요한 값을 검증
        requireText(request.getPassword(), "비밀번호");
        requireText(request.getTitle(), "schedule 제목");
        requireMaxLength(request.getTitle(), "schedule 제목", 30);
        requireText(request.getAuthorName(), "작성자이름");


        // 수정 대상 일정을 조회
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("일정이 없는데용?")
        );

        // 비밀번호 검증
        if (!schedule.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("님 비밀번호 틀렸음");
        }

        schedule.update(request.getTitle(), request.getAuthorName());

        Schedule saved = scheduleRepository.save(schedule);

        return new UpdateScheduleResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getContent(),
                saved.getAuthorName(),
                saved.getCreatedAt(),
                saved.getModifiedAt());
    }

    // 일정 삭제 (비밀번호 검증 필요)
    @Transactional
    public void delete(Long scheduleId, DeleteScheduleRequest request) {

        requireText(request.getPassword(), "비밀번호");

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("일정이 없는데용?")
        );

        if(!schedule.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("님 비밀번호 틀렸음");
        }

        scheduleRepository.delete(schedule);
    }
}
