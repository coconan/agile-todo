package me.coconan.agiletodo;

public class Stats {
    private final long total;
    private final long today;

    public Stats(Long total, Long today) {
        this.total = total;
        this.today = today;
    }

    public long getTotal() {
        return total;
    }

    public long getToday() {
        return today;
    }
}
