package com.malak.vaultprefs14.exportbox;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public final class ExternalExportBox {

    private ExternalExportBox() {}

    public static String exportText(Context context, String fileName, String content) throws Exception {
        File folder = context.getExternalFilesDir(null);

        if (folder == null) {
            return null;
        }

        File target = new File(folder, fileName);

        FileOutputStream stream = null;

        try {
            stream = new FileOutputStream(target, false);
            stream.write(content.getBytes(StandardCharsets.UTF_8));
            return target.getAbsolutePath();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public static String readExport(Context context, String fileName) throws Exception {
        File folder = context.getExternalFilesDir(null);

        if (folder == null) {
            return null;
        }

        File target = new File(folder, fileName);

        if (!target.exists()) {
            return null;
        }

        FileInputStream stream = null;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try {
            stream = new FileInputStream(target);

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
        File folder = context.getExternalFilesDir(null);

        if (folder == null) {
            return false;
        }

        File target = new File(folder, fileName);
        return target.delete();
    }
}
