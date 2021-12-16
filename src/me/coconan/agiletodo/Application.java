package me.coconan.agiletodo;

/**
 * mkdir -p out
 * javac src/me/coconan/agiletodo/Application.java -d out
 * java -classpath out me.coconan.agiletodo.Application ~/enduring-patience
 */
public class Application {

    public static void main(String[] args) {
        // get input directory
        String directory = args[0];
        System.out.printf("directory: %s\n", directory);
    }

}
