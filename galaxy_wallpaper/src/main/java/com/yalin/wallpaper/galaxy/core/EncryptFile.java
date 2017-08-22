package com.yalin.wallpaper.galaxy.core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

public class EncryptFile {
    public static final String KEY = "SdG4w323sdfvDfgHylljaASd42daWE34234REWEr46";

    public static String load(String path, String key, Boolean decrypt) throws Exception {
        String text = "";
        String k;
        if (key != "") {
            k = key;
        } else {
            k = KEY;
        }
        StringBuilder sb = new StringBuilder();
        String NL = System.getProperty("line.separator");
        if (decrypt.booleanValue()) {
            BufferedReader br = new BufferedReader(new FileReader(path));
            while (true) {
                try {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                } finally {
                    br.close();
                }
            }
            return SimpleCrypto.fromHex(sb.toString());
        }
        Scanner scanner = new Scanner(new FileInputStream(path));
        while (scanner.hasNextLine()) {
            try {
                sb.append(scanner.nextLine() + NL);
            } finally {
                scanner.close();
            }
        }
        return sb.toString();
    }

    public static String load(InputStream input, String key, Boolean decrypt) throws Exception {
        String text = "";
        String k;
        if (key != "") {
            k = key;
        } else {
            k = KEY;
        }
        StringBuilder sb = new StringBuilder();
        String NL = System.getProperty("line.separator");
        if (decrypt) {
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            try {
                while (true) {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    sb.append(line);
                }
            } finally {
                br.close();
            }
            return SimpleCrypto.fromHex(sb.toString());
        }
        Scanner scanner = new Scanner(input);
        while (scanner.hasNextLine()) {
            try {
                sb.append(scanner.nextLine() + NL);
            } finally {
                scanner.close();
            }
        }
        return sb.toString();
    }

    public static String write(String path, String key, String normalText, Boolean encrypt) throws Exception {
        String encryptText;
        String k;
        if (key != "") {
            k = key;
        } else {
            k = KEY;
        }
        if (encrypt.booleanValue()) {
            encryptText = SimpleCrypto.toHex(normalText);
        } else {
            encryptText = normalText;
        }
        Writer out = new OutputStreamWriter(new FileOutputStream(path));
        try {
            out.write(encryptText);
            return encryptText;
        } finally {
            out.close();
        }
    }
}
