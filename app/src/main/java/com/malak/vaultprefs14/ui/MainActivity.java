package com.malak.vaultprefs14.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.malak.vaultprefs14.R;
import com.malak.vaultprefs14.cachebox.EphemeralCacheStore;
import com.malak.vaultprefs14.exportbox.ExternalExportBox;
import com.malak.vaultprefs14.model.LearnerRecord;
import com.malak.vaultprefs14.prefs.ProfilePrefs;
import com.malak.vaultprefs14.prefs.SecretVaultPrefs;
import com.malak.vaultprefs14.storage.LearnerJsonStore;
import com.malak.vaultprefs14.storage.TextCapsuleStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "VaultPrefs14";

    private static final String NOTE_FILE = "lab14_private_note.txt";
    private static final String CACHE_FILE = "last_interface_snapshot.tmp";
    private static final String EXPORT_FILE = "lab14_external_export.txt";

    private final String[] locales = {"fr", "en", "ar"};

    private EditText inputAlias;
    private EditText inputSecret;
    private Spinner spinnerLocale;
    private Switch switchMood;
    private TextView outputConsole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProfilePrefs.ProfileSnapshot savedTheme = ProfilePrefs.loadProfile(this);

        if ("midnight".equals(savedTheme.mood)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        setContentView(R.layout.activity_main);

        inputAlias = findViewById(R.id.inputAlias);
        inputSecret = findViewById(R.id.inputSecret);
        spinnerLocale = findViewById(R.id.spinnerLocale);
        switchMood = findViewById(R.id.switchMood);
        outputConsole = findViewById(R.id.outputConsole);

        Button btnRememberProfile = findViewById(R.id.btnRememberProfile);
        Button btnRestoreProfile = findViewById(R.id.btnRestoreProfile);
        Button btnWriteFiles = findViewById(R.id.btnWriteFiles);
        Button btnReadFiles = findViewById(R.id.btnReadFiles);
        Button btnCache = findViewById(R.id.btnCache);
        Button btnExport = findViewById(R.id.btnExport);
        Button btnWipe = findViewById(R.id.btnWipe);

        configureSpinner();

        btnRememberProfile.setOnClickListener(v -> rememberProfile());
        btnRestoreProfile.setOnClickListener(v -> restoreProfile());
        btnWriteFiles.setOnClickListener(v -> createInternalFiles());
        btnReadFiles.setOnClickListener(v -> readInternalFiles());
        btnCache.setOnClickListener(v -> testTemporaryCache());
        btnExport.setOnClickListener(v -> exportAppSpecificFile());
        btnWipe.setOnClickListener(v -> wipeEverything());

        restoreProfile();
        switchMood.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String alias = inputAlias.getText().toString().trim();
            String locale = locales[spinnerLocale.getSelectedItemPosition()];
            String mood = isChecked ? "midnight" : "sunrise";

            ProfilePrefs.saveProfile(this, alias, locale, mood, false);

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
    }

    private void configureSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                locales
        );

        spinnerLocale.setAdapter(adapter);
    }

    private void rememberProfile() {
        String alias = inputAlias.getText().toString().trim();
        String locale = locales[spinnerLocale.getSelectedItemPosition()];
        String mood = switchMood.isChecked() ? "midnight" : "sunrise";
        String token = inputSecret.getText().toString();

        boolean saved = ProfilePrefs.saveProfile(this, alias, locale, mood, false);

        if (!token.trim().isEmpty()) {
            try {
                SecretVaultPrefs.keepToken(this, token);
            } catch (Exception exception) {
                showResult("Erreur : le coffre chiffré n’a pas pu enregistrer le token.\n"
                        + "Détail technique contrôlé : " + exception.getClass().getSimpleName());
                return;
            }
        }

        try {
            String cacheLine = "alias=" + alias + ", locale=" + locale + ", mood=" + mood;
            EphemeralCacheStore.saveTemporary(this, CACHE_FILE, cacheLine);
        } catch (Exception ignored) {
        }

        Log.d(TAG, "Profil sauvegardé : ok=" + saved
                + ", alias=" + alias
                + ", locale=" + locale
                + ", mood=" + mood
                + ", tokenLength=" + token.length());

        showResult(
                "✅ Préférences sauvegardées.\n\n"
                        + "Alias : " + alias + "\n"
                        + "Langue : " + locale + "\n"
                        + "Ambiance : " + mood + "\n\n"
                        + "Token : stocké uniquement dans EncryptedSharedPreferences.\n"
                        + "Aucune valeur sensible n’est affichée en clair."
        );
    }

    private void restoreProfile() {
        ProfilePrefs.ProfileSnapshot snapshot = ProfilePrefs.loadProfile(this);

        inputAlias.setText(snapshot.alias);
        spinnerLocale.setSelection(findLocaleIndex(snapshot.locale));
        switchMood.setChecked("midnight".equals(snapshot.mood));

        int tokenLength = 0;

        try {
            String token = SecretVaultPrefs.readToken(this);
            tokenLength = token == null ? 0 : token.length();
        } catch (Exception ignored) {
        }

        Log.d(TAG, "Profil chargé : alias=" + snapshot.alias
                + ", locale=" + snapshot.locale
                + ", mood=" + snapshot.mood
                + ", tokenLength=" + tokenLength);

        showResult(
                "📥 Chargement terminé.\n\n"
                        + "Alias : " + snapshot.alias + "\n"
                        + "Langue : " + snapshot.locale + "\n"
                        + "Ambiance : " + snapshot.mood + "\n"
                        + "Token length : " + tokenLength + "\n\n"
                        + "Le token n’est jamais loggé ni affiché en clair."
        );
    }

    private int findLocaleIndex(String locale) {
        for (int i = 0; i < locales.length; i++) {
            if (locales[i].equals(locale)) {
                return i;
            }
        }

        return 0;
    }

    private void createInternalFiles() {
        List<LearnerRecord> records = new ArrayList<>();
        records.add(new LearnerRecord(101, "Nour", "Cyberdéfense"));
        records.add(new LearnerRecord(102, "Adam", "Télécoms embarquées"));
        records.add(new LearnerRecord(103, "Lina", "Sécurité mobile"));

        try {
            LearnerJsonStore.saveRecords(this, records);

            TextCapsuleStore.writeText(
                    this,
                    NOTE_FILE,
                    "Note UTF-8 : sauvegarde interne réalisée avec MODE_PRIVATE."
            );
        } catch (Exception exception) {
            showResult("Erreur création fichiers : " + exception.getClass().getSimpleName());
            return;
        }

        Log.d(TAG, "Fichiers internes créés : "
                + LearnerJsonStore.JSON_FILE + ", " + NOTE_FILE);

        showResult(
                "🗂️ Fichiers internes créés.\n\n"
                        + "Fichier JSON : " + LearnerJsonStore.JSON_FILE + "\n"
                        + "Note texte : " + NOTE_FILE + "\n\n"
                        + "Chemin Device File Explorer :\n"
                        + "/data/data/com.malak.vaultprefs14/files/"
        );
    }

    private void readInternalFiles() {
        List<LearnerRecord> records = LearnerJsonStore.loadRecords(this);

        String note;

        try {
            note = TextCapsuleStore.readText(this, NOTE_FILE);
        } catch (Exception exception) {
            note = "(note absente ou supprimée)";
        }

        StringBuilder builder = new StringBuilder();

        builder.append("📖 Lecture des fichiers internes.\n\n");
        builder.append("Note : ").append(note).append("\n\n");
        builder.append("Nombre d’éléments JSON : ").append(records.size()).append("\n");

        for (LearnerRecord record : records) {
            builder.append("• ")
                    .append(record.number)
                    .append(" | ")
                    .append(record.displayName)
                    .append(" | ")
                    .append(record.track)
                    .append("\n");
        }

        Log.d(TAG, "Lecture JSON : records=" + records.size());

        showResult(builder.toString());
    }

    private void testTemporaryCache() {
        String alias = inputAlias.getText().toString().trim();

        String cacheContent = "snapshot_ui={alias=" + alias
                + ", selectedLocale=" + locales[spinnerLocale.getSelectedItemPosition()]
                + ", darkMode=" + switchMood.isChecked()
                + "}";

        try {
            EphemeralCacheStore.saveTemporary(this, CACHE_FILE, cacheContent);
            String restored = EphemeralCacheStore.readTemporary(this, CACHE_FILE);

            showResult(
                    "⚡ Cache temporaire testé.\n\n"
                            + "Fichier cache : " + CACHE_FILE + "\n"
                            + "Contenu restauré :\n"
                            + restored + "\n\n"
                            + "Chemin : /data/data/com.malak.vaultprefs14/cache/"
            );
        } catch (Exception exception) {
            showResult("Erreur cache : " + exception.getClass().getSimpleName());
        }
    }

    private void exportAppSpecificFile() {
        ProfilePrefs.ProfileSnapshot snapshot = ProfilePrefs.loadProfile(this);

        String exportContent =
                "Export app-specific - LAB 14\n"
                        + "Alias=" + snapshot.alias + "\n"
                        + "Locale=" + snapshot.locale + "\n"
                        + "Mood=" + snapshot.mood + "\n"
                        + "Token=NON_EXPORTÉ\n";

        try {
            String path = ExternalExportBox.exportText(this, EXPORT_FILE, exportContent);
            String readBack = ExternalExportBox.readExport(this, EXPORT_FILE);

            showResult(
                    "📤 Export externe app-specific terminé.\n\n"
                            + "Fichier : " + EXPORT_FILE + "\n"
                            + "Chemin : " + path + "\n\n"
                            + "Lecture de contrôle :\n"
                            + readBack + "\n"
                            + "Aucune permission de stockage public n’est nécessaire."
            );
        } catch (Exception exception) {
            showResult("Erreur export externe : " + exception.getClass().getSimpleName());
        }
    }

    private void wipeEverything() {
        ProfilePrefs.erase(this);

        try {
            SecretVaultPrefs.erase(this);
        } catch (Exception ignored) {
        }

        LearnerJsonStore.remove(this);
        TextCapsuleStore.remove(this, NOTE_FILE);

        int deletedCache = EphemeralCacheStore.purge(this);
        boolean externalDeleted = ExternalExportBox.remove(this, EXPORT_FILE);

        inputAlias.setText("");
        inputSecret.setText("");
        spinnerLocale.setSelection(0);
        switchMood.setChecked(false);

        Log.d(TAG, "Nettoyage complet exécuté. Aucun secret loggé.");

        showResult(
                "🧹 Nettoyage complet terminé.\n\n"
                        + "SharedPreferences : clear()\n"
                        + "EncryptedSharedPreferences : clear()\n"
                        + "JSON interne : supprimé\n"
                        + "Note interne : supprimée\n"
                        + "Cache purgé : " + deletedCache + " fichier(s)\n"
                        + "Export externe supprimé : " + externalDeleted + "\n\n"
                        + "Aucune donnée sensible n’a été exposée dans Logcat."
        );
    }

    private void showResult(String message) {
        outputConsole.setText(message);
    }
}