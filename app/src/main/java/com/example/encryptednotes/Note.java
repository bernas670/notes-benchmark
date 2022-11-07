package com.example.encryptednotes;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.util.Base64;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

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


    private static String SECRET_KEY = "aesEncryptionKey";
    private static byte[] INIT_VECTOR = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};


    private static Cipher getCipher(int mode) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR);
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(mode, keySpec, iv);

        return cipher;
    }

    private static String encrypt(String value) throws Exception {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
        byte[] encrypted = cipher.doFinal(value.getBytes());

        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    private static String decrypt(String value) throws Exception {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
        byte[] original = cipher.doFinal(Base64.decode(value, Base64.DEFAULT));

        return new String(original);
    }

    public void saveNote(Context context) {
        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(id + ".txt", Context.MODE_PRIVATE);
            String fileContent = encrypt(title + "\n" + content);
            fos.write(fileContent.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

            text = decrypt(sb.toString());

            int div = text.indexOf("\n");

            title = text.substring(0, div);
            content = text.substring(div + 1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Note(id, title, content);
    }

    public static void deleteNote(String id) {
        File file = new File(MainActivity.path + "/" + id + ".txt");
        file.getAbsoluteFile().delete();
    }
}
