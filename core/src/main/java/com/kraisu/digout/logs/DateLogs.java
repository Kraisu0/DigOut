package com.kraisu.digout.logs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateLogs {

    public static String dateLog(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDateTime.format(formatter);
    }

}
