package me.coconan.agiletodo;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Event {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Status status;
    private final String content;

    public enum Status {
        PENDING,
        DONE;
    }

    public Event(LocalDateTime start, LocalDateTime end, Status status, String content) {
        this.start = start;
        this.end = end;
        this.status = status;
        this.content = content;
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
        long deltaDay = ChronoUnit.DAYS.between(getEnd(), LocalDateTime.now());
        return 0 <= deltaDay && deltaDay <= ((LocalDateTime.now().getDayOfWeek().getValue() % 7) - (getEnd().getDayOfWeek().getValue() % 7));
    }

    public Status getStatus() {
        return status;
    }

    public String getContent() {
        return content;
    }
}
