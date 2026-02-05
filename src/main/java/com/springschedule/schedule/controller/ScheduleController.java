package com.springschedule.schedule.controller;

import com.springschedule.schedule.dto.*;
import com.springschedule.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 일정 생성
    @PostMapping
    public ResponseEntity<CreateScheduleResponse> create(@RequestBody CreateScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.save(request));
    }

    // 일정 단건 조회 (댓글 포함)
    @GetMapping("/{scheduleId}")
    public ResponseEntity<GetScheduleAndCommentsResponse> getSchedule(@PathVariable Long scheduleId) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.findOne(scheduleId));
    }

    // 전체 일정 조회 (작성자명 필터) authorName이 없으면 전체 조회를 하고 authorName이 있으면 해당 작성자의 일정만 조회
    // 정렬 기준은 내림차순
    @GetMapping
    public ResponseEntity<List<GetScheduleResponse>> getSchedules(
            @RequestParam(required = false) String authorName) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.findAll(authorName));
    }

    // 일정 수정 (비밀번호 필요)
    // 수정이 가능한 필드는 title, authorName 그리고 수정 시에는 modifiedAt이 갱신
    @PatchMapping("/{scheduleId}")
    public ResponseEntity<UpdateScheduleResponse> updateSchedule(
            @PathVariable Long scheduleId, @RequestBody UpdateScheduleRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(scheduleService.update(scheduleId, request));
    }

    // 일정 삭제 (비밀번호 필요)
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long scheduleId, @RequestBody DeleteScheduleRequest request
    ) {
        scheduleService.delete(scheduleId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
