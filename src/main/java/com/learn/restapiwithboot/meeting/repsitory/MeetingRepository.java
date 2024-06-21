package com.learn.restapiwithboot.meeting.repsitory;

import com.learn.restapiwithboot.meeting.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
