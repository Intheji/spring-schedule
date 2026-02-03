package com.springschedule.service;

import com.springschedule.dto.*;
import com.springschedule.entity.Schedule;
import com.springschedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public CreateScheduleResponse save(CreateScheduleRequest request) {

        Schedule schedule = new Schedule(
                request.getTitle(),
                request.getContent(),
                request.getAuthorName(),
                request.getPassword()
        );

        Schedule saved = scheduleRepository.save(schedule);

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
    public GetScheduleResponse findOne(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("일정이 없는데요?")
        );

        return new GetScheduleResponse(
                schedule.getId(),
                schedule.getTitle(),
                schedule.getContent(),
                schedule.getAuthorName(),
                schedule.getCreatedAt(),
                schedule.getModifiedAt()
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

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("일정이 없는데용?")
        );

        if (!schedule.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("님 비밀번호 틀렸음");
        }

        schedule.update(request.getTitle(), request.getAuthorName());

        return new UpdateScheduleResponse(schedule.getId());
    }

    @Transactional
    public void delete(Long scheduleId, DeleteScheduleRequest request) {

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new IllegalStateException("일정이 없는데용?")
        );

        if(!schedule.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("님 비밀번호 틀렸음");
        }

        scheduleRepository.delete(schedule);
    }
}
