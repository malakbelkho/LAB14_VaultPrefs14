package com.malak.vaultprefs14.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public final class SecretVaultPrefs {

    private static final String VAULT_NAME = "encrypted_secret_vault";
    private static final String KEY_SESSION_TOKEN = "session_token_ciphered";

    private SecretVaultPrefs() {}

    private static SharedPreferences openVault(Context context) throws Exception {
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        return EncryptedSharedPreferences.create(
                context,
                VAULT_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public static void keepToken(Context context, String token) throws Exception {
        openVault(context)
                .edit()
                .putString(KEY_SESSION_TOKEN, token)
                .apply();
    }

    public static String readToken(Context context) throws Exception {
        return openVault(context).getString(KEY_SESSION_TOKEN, "");
    }

    public static void erase(Context context) throws Exception {
        openVault(context).edit().clear().apply();
    }
}
