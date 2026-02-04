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

    @Transactional
    public CreateScheduleResponse save(CreateScheduleRequest request) {

        // 먼저 검증을 하고
        requireText(request.getTitle(), "schedule 제목");
        requireMaxLength(request.getTitle(), "schedule 제목", 30);
        requireText(request.getContent(),"schedule 내용");
        requireMaxLength(request.getContent(), "schedule 내용", 200);
        requireText(request.getAuthorName(), "작성자이름");
        requireText(request.getPassword(), "비밀번호");


        // 검증을 통과하면 저장한다
        Schedule schedule = new Schedule(
                request.getTitle(),
                request.getContent(),
                request.getAuthorName(),
                request.getPassword()
        );

        Schedule saved = scheduleRepository.save(schedule);

        // 응답을 하고 return 한다
        return new CreateScheduleResponse(
                saved.getId(),
                saved.getTitle(),
                saved.getContent(),
                saved.getAuthorName(),
                saved.getCreatedAt(),
                saved.getModifiedAt()
        );
    }

    @Transactional(readOnly = true)
    public GetScheduleAndCommentsResponse findOne(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("일정이 없는데용?")
        );

        List<Comment> comments = commentRepository.findByScheduleIdOrderByCreatedAtAsc(scheduleId);

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

    @Transactional(readOnly = true)
    public List<GetScheduleResponse> findAll(String authorName) {

        List<Schedule> schedules;

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

    @Transactional
    public UpdateScheduleResponse update(Long scheduleId, UpdateScheduleRequest request) {

        requireText(request.getPassword(), "비밀번호");
        requireText(request.getTitle(), "schedule 제목");
        requireMaxLength(request.getTitle(), "schedule 제목", 30);
        requireText(request.getAuthorName(), "작성자이름");


        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("일정이 없는데용?")
        );

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
