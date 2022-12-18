package fhtw.ode.messengerapp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ServerLogger {
    private File logFile;
    private BufferedWriter writer;

    public ServerLogger(String fileName) throws IOException {
        logFile = new File(fileName);
        writer = new BufferedWriter(new FileWriter(logFile, true));
    }

    public void log(String message) throws IOException {
        writer.write(message);
        writer.newLine();
        writer.flush();
    }

    public void close() throws IOException {
        writer.close();
    }
}

