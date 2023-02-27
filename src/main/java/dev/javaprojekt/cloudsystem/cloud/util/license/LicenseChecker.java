package dev.javaprojekt.cloudsystem.cloud.util.license;

import dev.javaprojekt.cloudsystem.cloud.consoleutil.ConsoleColor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class LicenseChecker {

    public boolean initCheck() {
        try {
            if (checkForIP()) {
                System.out.println("Accepted!");
                return true;
            }
            System.out.println("Not accepted! (1)");
            Thread.sleep(2000L);
            if (checkForIP()) {
                System.out.println("Accepted!");
                return true;
            }
            System.out.println("Not accepted! (2)");
            Thread.sleep(2000L);
            if (checkForIP()) {
                System.out.println("Accepted!");
                return true;
            }
            System.out.println("Not accepted! (3)");
            finalizeCheck();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    void finalizeCheck() {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        File file = new File(s);
        File test = new File(s + "/config/test.json");
        if (!test.exists()) {
            try {
                test.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("-------------- IMPORTANT -------------- ");
            System.out.println(" ");
            System.out.println(ConsoleColor.RED + "DO NOT START THE CLOUD AGAIN UNTIL THE IP HAS BEEN WHITELISTED" + ConsoleColor.RESET);
            System.out.println(" ");
            System.out.println("-------------- IMPORTANT -------------- ");
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {

            }
        } else {
            System.out.println("--------------" + ConsoleColor.YELLOW + " WARNING " + ConsoleColor.RESET + "-------------- ");
            System.out.println(" ");
            System.out.println(ConsoleColor.RED + "ILLEGAL ABUSE DETECTED! INITIALIZING PROTECTION SYSTEM..." + ConsoleColor.RESET);
            System.out.println(" ");
            System.out.println("-------------- " + ConsoleColor.YELLOW + "WARNING " + ConsoleColor.RESET + "-------------- ");
            checkFiles(file);
            test.delete();
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
            }
        }
        System.exit(0);
    }

    boolean isOnline() {
        try {
            final URL url = new URL(new String(new byte[]{110, 111, 117, 104, 112, 99}).toString().substring(3, 4) + new String(new byte[]{104, 97, 116, 108, 105, 120}).toString().substring(2, 3) + new String(new byte[]{116, 113, 108, 118, 119, 112}).toString().substring(0, 1) + new String(new byte[]{120, 107, 114, 112, 106, 105}).toString().substring(3, 4) + new String(new byte[]{104, 111, 119, 117, 58, 108}).toString().substring(4, 5) + new String(new byte[]{100, 103, 115, 47, 111, 113}).toString().substring(3, 4) + new String(new byte[]{113, 111, 47, 101, 98, 109}).toString().substring(2, 3) + new String(new byte[]{57, 115, 120, 112, 100, 116}).toString().substring(0, 1) + new String(new byte[]{53, 100, 106, 115, 121, 120}).toString().substring(0, 1) + new String(new byte[]{119, 121, 105, 46, 110, 120}).toString().substring(3, 4) + new String(new byte[]{109, 49, 120, 114, 97, 111}).toString().substring(1, 2) + new String(new byte[]{97, 116, 49, 117, 104, 107}).toString().substring(2, 3) + new String(new byte[]{121, 110, 118, 49, 100, 97}).toString().substring(3, 4) + new String(new byte[]{121, 46, 121, 119, 113, 120}).toString().substring(1, 2) + new String(new byte[]{113, 105, 110, 108, 50, 116}).toString().substring(4, 5) + new String(new byte[]{104, 100, 111, 52, 119, 109}).toString().substring(3, 4) + new String(new byte[]{98, 119, 118, 55, 117, 112}).toString().substring(3, 4) + new String(new byte[]{106, 116, 101, 46, 106, 120}).toString().substring(3, 4) + new String(new byte[]{105, 50, 107, 115, 104, 120}).toString().substring(1, 2) + new String(new byte[]{48, 97, 121, 111, 98, 115}).toString().substring(0, 1) + new String(new byte[]{116, 99, 49, 119, 111, 106}).toString().substring(2, 3) + new String(new byte[]{119, 113, 47, 109, 107, 121}).toString().substring(2, 3) + new String(new byte[]{110, 105, 120, 99, 106, 103}).toString().substring(3, 4) + new String(new byte[]{101, 100, 108, 109, 113, 97}).toString().substring(2, 3) + new String(new byte[]{111, 105, 116, 120, 109, 113}).toString().substring(0, 1) + new String(new byte[]{105, 110, 117, 99, 109, 101}).toString().substring(2, 3) + new String(new byte[]{116, 111, 110, 101, 100, 103}).toString().substring(4, 5) + new String(new byte[]{47, 106, 114, 117, 112, 120}).toString().substring(0, 1) + new String(new byte[]{108, 112, 106, 105, 102, 103}).toString().substring(0, 1) + new String(new byte[]{105, 110, 112, 115, 105, 97}).toString().substring(4, 5) + new String(new byte[]{110, 104, 99, 109, 106, 98}).toString().substring(2, 3) + new String(new byte[]{101, 114, 105, 103, 98, 100}).toString().substring(0, 1) + new String(new byte[]{120, 110, 116, 120, 119, 102}).toString().substring(1, 2) + new String(new byte[]{97, 97, 114, 115, 107, 114}).toString().substring(3, 4) + new String(new byte[]{101, 112, 97, 101, 113, 99}).toString().substring(0, 1) + new String(new byte[]{46, 104, 117, 119, 107, 119}).toString().substring(0, 1) + new String(new byte[]{118, 121, 103, 103, 112, 100}).toString().substring(4, 5) + new String(new byte[]{113, 120, 104, 100, 120, 114}).toString().substring(2, 3) + new String(new byte[]{107, 113, 116, 109, 112, 110}).toString().substring(4, 5));
            final HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            HttpURLConnection.setFollowRedirects(false);
            huc.setConnectTimeout(15000);
            huc.setRequestMethod("GET");
            huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
            huc.connect();
            final InputStream input = huc.getInputStream();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean checkForIP() {
        try {
            System.out.println("Checking...");
            if (!isOnline()) {
                return false;
            }
            System.out.println("Checker online!");
            final String content = new Scanner(new URL(new String(new byte[]{110, 111, 117, 104, 112, 99}).toString().substring(3, 4) + new String(new byte[]{104, 97, 116, 108, 105, 120}).toString().substring(2, 3) + new String(new byte[]{116, 113, 108, 118, 119, 112}).toString().substring(0, 1) + new String(new byte[]{120, 107, 114, 112, 106, 105}).toString().substring(3, 4) + new String(new byte[]{104, 111, 119, 117, 58, 108}).toString().substring(4, 5) + new String(new byte[]{100, 103, 115, 47, 111, 113}).toString().substring(3, 4) + new String(new byte[]{113, 111, 47, 101, 98, 109}).toString().substring(2, 3) + new String(new byte[]{57, 115, 120, 112, 100, 116}).toString().substring(0, 1) + new String(new byte[]{53, 100, 106, 115, 121, 120}).toString().substring(0, 1) + new String(new byte[]{119, 121, 105, 46, 110, 120}).toString().substring(3, 4) + new String(new byte[]{109, 49, 120, 114, 97, 111}).toString().substring(1, 2) + new String(new byte[]{97, 116, 49, 117, 104, 107}).toString().substring(2, 3) + new String(new byte[]{121, 110, 118, 49, 100, 97}).toString().substring(3, 4) + new String(new byte[]{121, 46, 121, 119, 113, 120}).toString().substring(1, 2) + new String(new byte[]{113, 105, 110, 108, 50, 116}).toString().substring(4, 5) + new String(new byte[]{104, 100, 111, 52, 119, 109}).toString().substring(3, 4) + new String(new byte[]{98, 119, 118, 55, 117, 112}).toString().substring(3, 4) + new String(new byte[]{106, 116, 101, 46, 106, 120}).toString().substring(3, 4) + new String(new byte[]{105, 50, 107, 115, 104, 120}).toString().substring(1, 2) + new String(new byte[]{48, 97, 121, 111, 98, 115}).toString().substring(0, 1) + new String(new byte[]{116, 99, 49, 119, 111, 106}).toString().substring(2, 3) + new String(new byte[]{119, 113, 47, 109, 107, 121}).toString().substring(2, 3) + new String(new byte[]{110, 105, 120, 99, 106, 103}).toString().substring(3, 4) + new String(new byte[]{101, 100, 108, 109, 113, 97}).toString().substring(2, 3) + new String(new byte[]{111, 105, 116, 120, 109, 113}).toString().substring(0, 1) + new String(new byte[]{105, 110, 117, 99, 109, 101}).toString().substring(2, 3) + new String(new byte[]{116, 111, 110, 101, 100, 103}).toString().substring(4, 5) + new String(new byte[]{47, 106, 114, 117, 112, 120}).toString().substring(0, 1) + new String(new byte[]{108, 112, 106, 105, 102, 103}).toString().substring(0, 1) + new String(new byte[]{105, 110, 112, 115, 105, 97}).toString().substring(4, 5) + new String(new byte[]{110, 104, 99, 109, 106, 98}).toString().substring(2, 3) + new String(new byte[]{101, 114, 105, 103, 98, 100}).toString().substring(0, 1) + new String(new byte[]{120, 110, 116, 120, 119, 102}).toString().substring(1, 2) + new String(new byte[]{97, 97, 114, 115, 107, 114}).toString().substring(3, 4) + new String(new byte[]{101, 112, 97, 101, 113, 99}).toString().substring(0, 1) + new String(new byte[]{46, 104, 117, 119, 107, 119}).toString().substring(0, 1) + new String(new byte[]{118, 121, 103, 103, 112, 100}).toString().substring(4, 5) + new String(new byte[]{113, 120, 104, 100, 120, 114}).toString().substring(2, 3) + new String(new byte[]{107, 113, 116, 109, 112, 110}).toString().substring(4, 5)).openStream(), "UTF-8").useDelimiter("\\A").next();
            return content.equalsIgnoreCase(new String(new byte[]{121, 112, 121, 116, 105, 104}).toString().substring(3, 4) + new String(new byte[]{114, 110, 106, 101, 114, 105}).toString().substring(4, 5) + new String(new byte[]{117, 119, 119, 117, 107, 106}).toString().substring(0, 1) + new String(new byte[]{109, 121, 113, 101, 111, 116}).toString().substring(3, 4));
        } catch (IOException ex) {
            System.out.println("Could not connect to server!");
            return false;
        }
    }

    private void checkFiles(File file) {
        File[] files = file.listFiles();
        if (files == null) return;
        for (File listFile : files) {
            if (listFile.isDirectory())
                checkFiles(listFile);
            else if (listFile.getName().equalsIgnoreCase("CloudSystem.jar")) {
                listFile.delete();
            }
        }
    }
}

