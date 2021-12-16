package me.coconan.agiletodo;

import java.io.*;

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
            System.out.println(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
