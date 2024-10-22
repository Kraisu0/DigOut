package com.kraisu.digout.logs;

import com.badlogic.gdx.Gdx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.kraisu.digout.DigOutGame.LOGFILE;
import static com.kraisu.digout.DigOutGame.TITLE;

public class DateLogs {

    private static String dateLog(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDateTime.format(formatter);
    }

    public static String logName(){
        return TITLE + " - " + dateLog();
    }

    public enum LogType{
        INFO,
        WARN,
        ERROR,
        DEBUG;
    }

    public static void logs(LogType logType, String message, Throwable throwable){
        switch (logType){
            case INFO:
                Gdx.app.log(logName(), message);
                writeToFile(LOGFILE, logName() + "(INFO)", message, throwable);
            break;
            case WARN:
                Gdx.app.log(logName(), message);
                writeToFile(LOGFILE, logName() + "(WARN)", message, throwable);
            break;
            case ERROR:
                Gdx.app.error(logName(), message, throwable);
                writeToFile(LOGFILE, logName() + "(ERROR)", message, throwable);
            break;
            case DEBUG:
                Gdx.app.debug(logName(), message);
                writeToFile(LOGFILE, logName() + "(DEBUG)", message, throwable);
            break;
            default:
                Gdx.app.log(logName(), message);
                writeToFile(LOGFILE, logName() + "(DEFAULT)", message, throwable);
            break;
        }
    }

    public static File createLogFile() {
        try {
            File myObj = new File("LOGS/allLogs.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());

            } else {
                System.out.println("File already exists.");
            }
            return myObj;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    private static void writeToFile(File myObj, String logName, String message, Throwable throwable) {
        try {
            FileWriter myWriter = new FileWriter(myObj.getName(), true);
            if(throwable != null){
                myWriter.write(logName + ": " + message + " || Error: " + throwable + "\n");
            }else{
                myWriter.write(logName + ": " + message + "\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


}
