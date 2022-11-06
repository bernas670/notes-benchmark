package com.example.encryptednotes;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {

    private String id, title, content;

    public Note(String title, String content) {
        this.title = title;
        this.content = content;

        Date currentDate = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        id = ft.format(currentDate);
    }

    public Note(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public String getId() { return this.id; }
    public String getTitle() { return this.title; }
    public String getContent() { return this.content; }

    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }

    public static Note loadNote(Context context, String id) {
        String title = "", content = "";
        FileInputStream fis = null;

        try {
            fis = context.openFileInput(id + ".txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();

            String text;
            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            text = sb.toString();
            int div = text.indexOf("\n");

            title = text.substring(0, div);
            content = text.substring(div + 1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Note(id, title, content);
    }

    public static void deleteNote(String id) {
        File file = new File(MainActivity.path + "/" + id + ".txt");
        file.getAbsoluteFile().delete();
    }

    public void saveNote(Context context) {
        // TODO: create path if it does not exist

        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(id + ".txt", Context.MODE_PRIVATE);
            String fileContent = title + "\n" + content;
            fos.write(fileContent.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
