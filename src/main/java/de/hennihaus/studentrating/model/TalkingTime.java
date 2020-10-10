package de.hennihaus.studentrating.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TalkingTime {

    private Campus campus;
    private String room;
    private Day day;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public TalkingTime(TalkingTimeWrapper talkingTime) {
        campus = talkingTime.getCampus();
        room = talkingTime.getRoom();
        day = talkingTime.getDay();
        startTime  = talkingTime.getStartTime();
        endTime = talkingTime.getEndTime();
    }

    @Setter
    @Getter
    @Accessors(chain = true)
    public static class TalkingTimeWrapper {

        private Campus campus;
        private String room;
        private Day day;
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        public TalkingTime wrap() {
            return new TalkingTime(this);
        }
    }
}
