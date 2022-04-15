package me.coconan.agiletodo;

import java.util.ArrayList;
import java.util.List;

public class Stats {
    private final long total;
    private final long today;
    private final List<Long> week;

    public Stats(Long total, Long today) {
        this.total = total;
        this.today = today;
        this.week = new ArrayList<>(7);
        for (int i = 0; i < 7; i++) {
            this.week.add(0L);
        }
    }

    public long getTotal() {
        return total;
    }

    public long getToday() {
        return today;
    }

    public void addWeekItem(int index, long count) {
        week.set(index, count + week.get(index));
    }

    public List<Long> getWeek() {
        return week;
    }
}
