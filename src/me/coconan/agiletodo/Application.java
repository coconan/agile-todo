package me.coconan.agiletodo;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
            System.out.println(filename);
        }

        // find spring events.md
        File events = new File(directory + "/pearls/spring/events.md");
        System.out.println(events.getAbsolutePath());
        System.out.println("exists: " + events.exists());

        // read file content
        try {
            BufferedReader reader = new BufferedReader(new FileReader(events));
            List<Event> eventList = new ArrayList<>();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                // parse event
                Event event = parse(line);
                eventList.add(event);
            }
            System.out.println(count(eventList));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private static int count(List<Event> eventList) {
        int totalInMinute = 0;
        for (Event event : eventList) {
            totalInMinute += ChronoUnit.MINUTES.between(event.getEnd(), event.getStart());
        }
        return totalInMinute;
    }
}
