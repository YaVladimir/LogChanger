package ru.sbt.corp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    private static final String FILE_NAME_ONE = "D:\\Downloads\\dul-module0.log";
    private static final String FILE_NAME_TWO = "D:\\Downloads\\dul-module1.log";
    private static final String FILE_NAME_RESULT = "C:\\Users\\Yakovenko\\IdeaProjects\\LogChanger\\res\\result.txt";

    public static void main(String[] args) {
        try {
            List<String> listOne = Files.readAllLines(Paths.get(FILE_NAME_ONE), StandardCharsets.UTF_8);
            List<String> logsOne = getLogs(listOne);
            List<String> listTwo = Files.readAllLines(Paths.get(FILE_NAME_TWO), StandardCharsets.UTF_8);
            List<String> logsTwo = getLogs(listTwo);
            //logs.forEach(System.out::println);

            logsOne.addAll(logsTwo);
            logsOne.sort((o1, o2) -> {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date parseDateOne = format.parse(o1, new ParsePosition(0));
                Date parseDateTwo = format.parse(o2, new ParsePosition(0));
                return parseDateOne.compareTo(parseDateTwo);
            });

            //Files.createFile(Paths.get(FILE_NAME_RESULT));
            Files.write(Paths.get(FILE_NAME_RESULT), logsOne, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static List<String> getLogs(List<String> list) {
        int j = 0;
        List<String> logs = new ArrayList<>();
        for (String aList : list) {
            if (aList.startsWith("2018") && !aList.equals("")) {
                logs.add(j, aList);
                j++;
            } else {
                String newString = logs.get(j - 1);
                newString = newString.concat("\n");
                newString = newString.concat(aList);
                logs.remove(j - 1);
                logs.add(j - 1, newString);
            }
        }
        return modifyLogs(logs);
    }

    private static List<String> modifyLogs(List<String> list) {
        int j = 0;
        List<String> modifyList = new ArrayList<>();
        modifyList.add(list.get(0));
        for (int i = 1; i < list.size(); i++) {
            StringBuilder oldStringBuilder = new StringBuilder(list.get(i));
            String oldDate = oldStringBuilder.substring(0, 19);
            StringBuilder newStringBuilder = new StringBuilder(modifyList.get(j));
            String newDate = newStringBuilder.substring(0, 19);
            if (newDate.equals(oldDate)) {
                String newString = modifyList.get(j);
                newString = newString.concat("\n");
                newString = newString.concat(list.get(i));
                modifyList.remove(j);
                modifyList.add(j, newString);
            } else {
                modifyList.add(list.get(i));
                j++;
            }
        }
        return modifyList;
    }
}
