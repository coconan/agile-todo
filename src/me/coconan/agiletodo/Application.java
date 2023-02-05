package me.coconan.agiletodo;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * AgileTodo
 *
 * Usage
 * $./run.sh
 */
public class Application {

    public static void main(String[] args) {
        // get input directory
        String directory = args[0];
        System.out.printf("%-64s %8s %5s", "directory: " + directory, "total", "today");
        for (int i = 0; i < 7; i++) {
            System.out.printf("%5s", DayOfWeek.of(i == 0 ? 7 : i).getDisplayName(TextStyle.SHORT, Locale.US));
        }
        System.out.println();

        // list directory files
        File dir = new File(directory);
        for (String filename : dir.list()) {
            File target = new File(directory + "/" + filename);
            if (target.isDirectory()) {
                File events = new File(target.getAbsolutePath() + "/events.md");
                if (events.exists()) {
                    try {
                        List<Event> eventList = processEvents(target.getAbsolutePath() + "/events.md");
                        if ("status".equals(args[1])) {
                            Stats stats = count(eventList);
                            System.out.printf("%-64s %8d %+5d", events.getAbsolutePath() + ":", stats.getTotal(), stats.getToday());
                            for (Long count : stats.getWeek()) {
                                System.out.printf("%5d", count);
                            }
                            System.out.println();
                        } else if ("todo".equals(args[1])) {
                            eventList = eventList.stream().filter(event -> event.getStatus() == Event.Status.PENDING).collect(Collectors.toList());
                            if (eventList.isEmpty()) {
                                continue;
                            }
                            System.out.printf("%-64s\n", events.getAbsolutePath());
                            for (Event event : eventList) {
                                if (event.getStatus() == Event.Status.PENDING) {
                                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                    System.out.printf("%s - %s %s\n", df.format(event.getStart()), df.format(event.getEnd()), event.getContent());
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(target.getAbsolutePath() + "/events.md" + ": 1 error");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static List<Event> processEvents(String filePath) throws IOException {
        File events = new File(filePath);

        // read file content
        BufferedReader reader = new BufferedReader(new FileReader(events));
        List<Event> eventList = new ArrayList<>();
        while (true) {
            String line = reader.readLine();
            if (line == null || line.trim().isEmpty()) {
                break;
            }

            // parse event
            Event event = parse(line);
            eventList.add(event);
        }
        return eventList;
    }

    private static Event parse(String line) {
        String[] parts = line.split("  ");
        String timeInterval = parts[0].substring(2);
        Event.Status status = Event.Status.DONE;
        String content = parts[1];
        if (parts[0].contains("[")) {
            timeInterval = parts[1];
            status = parts[0].contains("X") ? Event.Status.DONE : Event.Status.PENDING;
            content = parts[2];
        }
        String[] timeParts = timeInterval.split(" - ");
        String start = timeParts[0];
        String end = timeParts[1];
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDateTime = LocalDateTime.parse(start, dateTimeFormatter);
        LocalDateTime endDateTime = LocalDateTime.parse(end, dateTimeFormatter);
        return new Event(startDateTime, endDateTime, status, content);
    }

    private static Stats count(List<Event> eventList) {
        long totalInMinute = 0L;
        long todayMinute = 0L;
        for (Event event : eventList) {
            long duration = event.getDuration();
            if (duration < 0) {
                throw new RuntimeException("");
            }
            totalInMinute += duration;
            if (event.getEnd().isAfter(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))) {
                todayMinute += duration;
            }
        }
        Stats stats = new Stats(totalInMinute, todayMinute);

        for (Event event : eventList) {
            if (event.isCurrentWeek()) {
                stats.addWeekItem(event.getEnd().getDayOfWeek().getValue() % 7, event.getDuration());
            }
        }

        return stats;
    }
}
