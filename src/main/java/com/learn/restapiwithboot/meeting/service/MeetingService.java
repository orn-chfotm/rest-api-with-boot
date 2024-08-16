package com.learn.restapiwithboot.meeting.service;

import com.learn.restapiwithboot.account.domain.Account;
import com.learn.restapiwithboot.account.repository.AccountRepository;
import com.learn.restapiwithboot.core.exceptions.enums.impl.AccountErrorType;
import com.learn.restapiwithboot.core.exceptions.enums.impl.ResourceErrorType;
import com.learn.restapiwithboot.core.exceptions.exception.ext.BasicException;
import com.learn.restapiwithboot.meeting.domain.Meeting;
import com.learn.restapiwithboot.meeting.dto.request.MeetingRequest;
import com.learn.restapiwithboot.meeting.dto.response.MeetingResponse;
import com.learn.restapiwithboot.meeting.mapper.MeetingMapper;
import com.learn.restapiwithboot.meeting.repsitory.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MeetingService {

    private final AccountRepository accountRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingMapper meetingMapper;

    public Page<MeetingResponse> getAllMeeting(Pageable pageable) {
        Page<Meeting> allMeeting = meetingRepository.findAll(pageable);

        List<MeetingResponse> collect = allMeeting.getContent().stream()
                .map(meetingMapper::meetingToMeetingResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(collect, pageable, allMeeting.getSize());
    }

    public MeetingResponse getMeeting(Long id) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new BasicException(ResourceErrorType.RESOURCE_MEETING_NOT_FOUND));

        return meetingMapper.meetingToMeetingResponse(meeting);
    }

    @Transactional
    public MeetingResponse createMeeting(Long accountId, MeetingRequest meetingRequest) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BasicException(AccountErrorType.ACCOUNT_NOT_FOUND));

        Meeting meeting = meetingMapper.meetingRequestToMeeting(meetingRequest);
        meeting.setAccount(account);

        Meeting savedMeeting = meetingRepository.save(meeting);

        return meetingMapper.meetingToMeetingResponse(savedMeeting);
    }

    @Transactional
    public MeetingResponse updateMeeting(Long id, Long accountId, MeetingRequest meetingRequest) {
        Meeting meeting = meetingRepository.findByIdAndAccountId(id, accountId)
                .orElseThrow(() -> new BasicException(ResourceErrorType.RESOURCE_MEETING_NOT_FOUND));

        meetingMapper.updateMeetingFromRequest(meetingRequest, meeting);

        return meetingMapper.meetingToMeetingResponse(meeting);
    }

    @Transactional
    public void deleteMeeting(Long id, Long accountId) {
        if (!meetingRepository.existsById(id)) {
            throw new BasicException(ResourceErrorType.RESOURCE_MEETING_NOT_FOUND);
        }
        if (!meetingRepository.existsByIdAndAccountId(id, accountId)) {
            throw new BasicException(ResourceErrorType.RESOURCE_MEETING_NOT_FOUND);
        }
        meetingRepository.deleteById(id);
    }
}
