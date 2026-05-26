package com.malak.vaultprefs14.cachebox;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public final class EphemeralCacheStore {

    private EphemeralCacheStore() {}

    public static void saveTemporary(Context context, String fileName, String content) throws Exception {
        File target = new File(context.getCacheDir(), fileName);

        FileOutputStream stream = null;

        try {
            stream = new FileOutputStream(target, false);
            stream.write(content.getBytes(StandardCharsets.UTF_8));
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public static String readTemporary(Context context, String fileName) throws Exception {
        File target = new File(context.getCacheDir(), fileName);

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

    public static int purge(Context context) {
        File[] cachedFiles = context.getCacheDir().listFiles();

        if (cachedFiles == null) {
            return 0;
        }

        int removed = 0;

        for (File file : cachedFiles) {
            if (file.delete()) {
                removed++;
            }
        }

        return removed;
    }
}
