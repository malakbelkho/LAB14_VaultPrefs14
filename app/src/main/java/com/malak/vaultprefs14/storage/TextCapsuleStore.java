package com.malak.vaultprefs14.storage;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public final class TextCapsuleStore {

    private TextCapsuleStore() {}

    public static void writeText(Context context, String fileName, String content) throws Exception {
        FileOutputStream stream = null;

        try {
            stream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            stream.write(content.getBytes(StandardCharsets.UTF_8));
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public static String readText(Context context, String fileName) throws Exception {
        FileInputStream stream = null;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try {
            stream = context.openFileInput(fileName);

            byte[] chunk = new byte[1024];
            int count;

            while ((count = stream.read(chunk)) != -1) {
                buffer.write(chunk, 0, count);
            }

            return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public static boolean remove(Context context, String fileName) {
        return context.deleteFile(fileName);
    }
}
