package me.coconan.agiletodo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Event {
    private final LocalDateTime start;
    private final LocalDateTime end;

    public Event(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public long getDuration() {
        return ChronoUnit.MINUTES.between(getStart(), getEnd());
    }

    public boolean isCurrentWeek() {
        long deltaDay = ChronoUnit.DAYS.between(LocalDateTime.now(), getEnd().plus(1, ChronoUnit.WEEKS));
        return 0 < deltaDay && deltaDay < 7;
    }
}
