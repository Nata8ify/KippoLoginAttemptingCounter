/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kippolacounter;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author PNattawut
 */
public class KippoLACounter {

    /**
     * @param args the command line arguments
     */
    private static Map<String, Integer> mostUsernameMap;
    private static Map<String, Integer> mostPasswordMap;
    private static int counter;
    static int kippoIndex = 0;
    static int attempCount = 0;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        System.out.println("Please be ensure there are a series of kippo.log in this directory or else is exception throwing...");
        System.out.println("Enter 'Y' to continue...");
        Scanner sc = new Scanner(System.in);
        if(sc.nextLine().equalsIgnoreCase("y")){
            execute();
        }
    }
    
    private static void execute() throws FileNotFoundException, IOException{
        mostUsernameMap = new LinkedHashMap<>();
        mostPasswordMap = new LinkedHashMap<>();

        Scanner scanner = new Scanner(new File("kippo.log"));
        while (true) {
            try{
            while (scanner.hasNextLine()) {
                String attemptLine = scanner.nextLine();
                if (attemptLine.contains("login attempt")) {
                    ++attempCount;
                    String username = attemptLine.substring(attemptLine.indexOf("[", 30) + 1, attemptLine.indexOf("/"));
                    String password = attemptLine.substring(attemptLine.indexOf("/") + 1, attemptLine.indexOf("]", attemptLine.indexOf("/")));
                    // Username Counter
                    if (!mostUsernameMap.containsKey(username)) {
                        mostUsernameMap.put(username, 1);
                    } else {
                        mostUsernameMap.put(username, mostUsernameMap.get(username) + 1);
                    }
                    //Password Counter
                    if (!mostPasswordMap.containsKey(password)) {
                        mostPasswordMap.put(password, 1);
                    } else {
                        mostPasswordMap.put(password, mostPasswordMap.get(password) + 1);
                    }
                }
            }
            ++kippoIndex;
            scanner = new Scanner(new File("kippo.log." + kippoIndex));
            } catch(Exception e){
                break;
            }
        }

        mostUsernameMap = sort(mostUsernameMap);
        mostPasswordMap = sort(mostPasswordMap);
        out(mostUsernameMap, 0, "username");
        out(mostPasswordMap, 0, "password");
//        System.out.println("Username ---");
//        for (Map.Entry<String, Integer> usernameMap : mostUsernameMap.entrySet()) {
//            System.out.println(usernameMap.getKey() + " (" + usernameMap.getValue() + ")");
////            ++count;
////            if(usernameMap.getValue() < 100){
////                break;
////            }
//        }
//        System.out.println("\n\nPassword ---");
//        for (Map.Entry<String, Integer> passwordMap : mostPasswordMap.entrySet()) {
//            System.out.println(passwordMap.getKey() + " (" + passwordMap.getValue() + ")");
////            ++count;
////            if(passwordMap.getValue() < 200){
////                break;
////            }
//        }

        System.out.println("Total attempts : " + attempCount);
    }

    private static Map<String, Integer> sort(Map map) {
        List<Map.Entry<String, Integer>> mapList = new LinkedList<>(map.entrySet());
        Collections.sort(mapList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> t, Map.Entry<String, Integer> t1) {
                return t1.getValue().compareTo(t.getValue());
            }
        });
        Map<String, Integer> sortedValueMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> orderMap : mapList) {
            sortedValueMap.put(orderMap.getKey(), orderMap.getValue());
        }
        return sortedValueMap;
    }

    private static void out(Map<String, Integer> outMap, long limit, String logName) throws FileNotFoundException, IOException{
        counter = 0;
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(logName.concat(".txt")));
         for (Map.Entry<String, Integer> map : outMap.entrySet()) {
            //System.out.println(map.getKey() + " (" + map.getValue() + ")");
            dos.writeUTF(map.getKey() + " (" + map.getValue() + ") \n");
            if(counter == 0){continue;}
            ++counter;
            if(map.getValue() < counter){
                break;
            }
        }
         dos.close();
    }
    
}
