package me.coconan.agiletodo;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * mkdir -p out
 * javac -classpath {JAVA_8_HOME}/jre/lib/rt.jar src/me/coconan/agiletodo/Application.java -d out
 * java -classpath {JAVA_8_HOME}/jre/lib/rt.jar:./out me.coconan.agiletodo.Application ~/enduring-patience
 */
public class Application {

    public static void main(String[] args) {
        // get input directory
        String directory = args[0];
        System.out.printf("directory: %s\n", directory);

        // list directory files
        File dir = new File(directory);
        for (String filename : dir.list()) {
            File target = new File(directory + "/" + filename);
            if (target.isDirectory()) {
                File events = new File(target.getAbsolutePath() + "/events.md");
                if (events.exists()) {
                    try {
                        processEvents(target.getAbsolutePath() + "/events.md");
                    } catch (Exception e) {
                        System.out.println(target.getAbsolutePath() + "/events.md" + ": 1 error");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void processEvents(String filePath) throws IOException {
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
        System.out.printf("%-64s %10s\n", events.getAbsolutePath() + ":", count(eventList));
    }

    private static Event parse(String line) {
        String[] parts = line.split("  ");
        String timeInterval = parts[0].substring(2);
        String[] timeParts = timeInterval.split(" - ");
        String start = timeParts[0];
        String end = timeParts[1];
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDateTime = LocalDateTime.parse(start, dateTimeFormatter);
        LocalDateTime endDateTime = LocalDateTime.parse(end, dateTimeFormatter);
        return new Event(startDateTime, endDateTime);
    }

    private static String count(List<Event> eventList) {
        long totalInMinute = 0L;
        long todayMinute = 0L;
        for (Event event : eventList) {
            long duration = ChronoUnit.MINUTES.between(event.getStart(), event.getEnd());
            if (duration < 0) {
                throw new RuntimeException("");
            }
            totalInMinute += duration;
            if (event.getEnd().isAfter(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS))) {
                todayMinute += duration;
            }
        }
        return totalInMinute + " +" + todayMinute;
    }
}
