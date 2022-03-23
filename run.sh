#!/bin/sh
mkdir -p out
javac -classpath {JAVA_8_HOME}/jre/lib/rt.jar src/me/coconan/agiletodo/Application.java -d out
java -classpath {JAVA_8_HOME}/jre/lib/rt.jar:./out me.coconan.agiletodo.Application ~/enduring-patience