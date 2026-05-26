package com.malak.vaultprefs14.storage;

import android.content.Context;

import com.malak.vaultprefs14.model.LearnerRecord;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class LearnerJsonStore {

    public static final String JSON_FILE = "learners_vault_records.json";

    private LearnerJsonStore() {}

    public static void saveRecords(Context context, List<LearnerRecord> records) throws Exception {
        JSONArray array = new JSONArray();

        for (LearnerRecord record : records) {
            JSONObject item = new JSONObject();
            item.put("number", record.number);
            item.put("displayName", record.displayName);
            item.put("track", record.track);
            array.put(item);
        }

        TextCapsuleStore.writeText(context, JSON_FILE, array.toString());
    }

    public static List<LearnerRecord> loadRecords(Context context) {
        List<LearnerRecord> records = new ArrayList<>();

        try {
            String rawJson = TextCapsuleStore.readText(context, JSON_FILE);
            JSONArray array = new JSONArray(rawJson);

            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);

                records.add(new LearnerRecord(
                        item.getInt("number"),
                        item.getString("displayName"),
                        item.getString("track")
                ));
            }
        } catch (Exception ignored) {
            return new ArrayList<>();
        }

        return records;
    }

    public static boolean remove(Context context) {
        return context.deleteFile(JSON_FILE);
    }
}