package com.kraisu.digout.logs;

import com.badlogic.gdx.Gdx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

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

    public static void logs(LogType logType, UUID uuid, String message, Throwable throwable){

        String messGdxLog = "blank";
        String messFileLog = "blank";

        if(uuid != null){
            messGdxLog = "(GAME_ID: " + uuid + ") " + message;
            messFileLog = "[GAME_ID: " + uuid + "] " + message;
        }else {
            messGdxLog = message;
            messFileLog = message;
        }


        switch (logType){
            case INFO:
                Gdx.app.log(logName(), messGdxLog);
                writeToFile(LOGFILE, logName() + "(INFO)", messFileLog, throwable);
            break;
            case WARN:
                Gdx.app.log(logName(), messGdxLog);
                writeToFile(LOGFILE, logName() + "(WARN)", messFileLog, throwable);
            break;
            case ERROR:
                Gdx.app.error(logName(), messGdxLog, throwable);
                writeToFile(LOGFILE, logName() + "(ERROR)", messFileLog, throwable);
            break;
            case DEBUG:
                Gdx.app.debug(logName(), messGdxLog);
                writeToFile(LOGFILE, logName() + "(DEBUG)", messFileLog, throwable);
            break;
            default:
                Gdx.app.log(logName(), messGdxLog);
                writeToFile(LOGFILE, logName() + "(DEFAULT)", messFileLog, throwable);
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
