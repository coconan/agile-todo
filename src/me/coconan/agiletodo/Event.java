package me.coconan.agiletodo;

import java.time.LocalDateTime;

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
}
