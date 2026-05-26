package com.malak.vaultprefs14.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public final class ProfilePrefs {

    private static final String BOX_NAME = "profile_canvas_box";
    private static final String KEY_ALIAS = "alias_value";
    private static final String KEY_LOCALE = "locale_choice";
    private static final String KEY_MOOD = "visual_mood";

    private ProfilePrefs() {}

    public static boolean saveProfile(
            Context context,
            String alias,
            String locale,
            String mood,
            boolean useCommit
    ) {
        SharedPreferences prefs = context.getSharedPreferences(BOX_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit()
                .putString(KEY_ALIAS, alias)
                .putString(KEY_LOCALE, locale)
                .putString(KEY_MOOD, mood);

        if (useCommit) {
            return editor.commit();
        }

        editor.apply();
        return true;
    }

    public static ProfileSnapshot loadProfile(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(BOX_NAME, Context.MODE_PRIVATE);

        String alias = prefs.getString(KEY_ALIAS, "");
        String locale = prefs.getString(KEY_LOCALE, "fr");
        String mood = prefs.getString(KEY_MOOD, "sunrise");

        return new ProfileSnapshot(alias, locale, mood);
    }

    public static void erase(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(BOX_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    public static final class ProfileSnapshot {
        public final String alias;
        public final String locale;
        public final String mood;

        public ProfileSnapshot(String alias, String locale, String mood) {
            this.alias = alias;
            this.locale = locale;
            this.mood = mood;
        }
    }
}
