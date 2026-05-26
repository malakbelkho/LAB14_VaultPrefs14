# VaultPrefs14 – LAB 14

Application Android développée en Java permettant de gérer la **persistance locale des données** à travers plusieurs mécanismes :  
**SharedPreferences**, **EncryptedSharedPreferences**, fichiers internes, cache temporaire et stockage externe app-specific.

L’application met l’accent sur les **bonnes pratiques de sécurité**, notamment le chiffrement des secrets, l’absence de logs sensibles et le nettoyage contrôlé des données locales.

---

## Objectif:

Le but de ce laboratoire est de :

- Comprendre le fonctionnement de la sauvegarde locale sous Android
- Manipuler les **SharedPreferences** pour stocker des préférences non sensibles
- Comparer l’utilisation de `apply()` et `commit()`
- Stocker un token de manière sécurisée avec **EncryptedSharedPreferences**
- Utiliser une **MasterKey** basée sur Android Keystore
- Écrire et lire des fichiers internes en UTF-8
- Sauvegarder et charger des données sous forme JSON
- Utiliser le cache interne pour des données temporaires
- Exporter un fichier dans le stockage externe app-specific
- Appliquer une checklist de sécurité : logs propres, chiffrement, nettoyage et séparation des données sensibles

---

## Description de l’application:

L’application **VaultPrefs14** propose un écran unique moderne permettant de tester plusieurs types de stockage local.

L’interface contient :

- Un champ pour le nom utilisateur
- Un sélecteur de langue
- Un switch pour le mode nuit
- Un champ sécurisé pour saisir un token
- Des boutons pour tester chaque mécanisme de stockage
- Une zone de résultat affichant les opérations réalisées

Les données non sensibles sont sauvegardées dans des préférences classiques, tandis que le token est stocké séparément dans un coffre chiffré.

---

## Fonctionnalités:

- Sauvegarde du profil utilisateur avec `SharedPreferences`
- Chargement automatique des préférences sauvegardées
- Sauvegarde sécurisée d’un token avec `EncryptedSharedPreferences`
- Masquage du token à l’écran avec `textPassword`
- Affichage uniquement de la longueur du token, jamais sa valeur
- Écriture d’un fichier texte interne en UTF-8
- Sauvegarde d’une liste d’objets dans un fichier JSON
- Lecture et affichage du contenu JSON
- Création d’un fichier temporaire dans le cache
- Purge manuelle du cache
- Export d’un fichier dans le stockage externe app-specific
- Nettoyage complet des préférences, secrets, fichiers internes, cache et export
- Interface personnalisée avec :
  - Dégradé de fond
  - Header coloré
  - Cartes arrondies
  - Inputs modernes
  - Boutons en dégradé
  - Console de résultat sombre
  - Mode nuit visuel

---

## Technologies utilisées:

- Android Studio
- Java
- XML
- Android SDK
- API minimum : 24
- SharedPreferences
- EncryptedSharedPreferences
- MasterKey
- AndroidX Security Crypto
- Internal Storage
- Cache Directory
- App-specific External Storage
- JSON avec `org.json`

---

## Aperçu de l’application:

▶️ Une démonstration vidéo complète est disponible dans le dossier **Demo** du repository.

⚠️ En cas de problème de lecture depuis le repository :

👉 [▶️ Voir la démo sur Google Drive](https://drive.google.com/file/d/TON_ID_DE_VIDEO/view?usp=sharing)

> Remarque : le lien Google Drive sera ajouté après l’upload final de la vidéo.

---

## Structure du projet:

```text
app/src/main/java/com/malak/vaultprefs14/
│
├── ui/
│   └── MainActivity.java
│
├── prefs/
│   ├── ProfilePrefs.java
│   └── SecretVaultPrefs.java
│
├── storage/
│   ├── TextCapsuleStore.java
│   └── LearnerJsonStore.java
│
├── cachebox/
│   └── EphemeralCacheStore.java
│
├── exportbox/
│   └── ExternalExportBox.java
│
└── model/
    └── LearnerRecord.java
```
## Layouts:

### `activity_main.xml`

Ce fichier représente l’écran principal de l’application.

Il contient :

- Un header visuel avec le nom de l’application
- Une carte pour les préférences utilisateur
- Un champ pour le nom
- Un spinner pour la langue
- Un switch pour le mode nuit
- Un champ sécurisé pour le token
- Des boutons pour chaque opération de stockage
- Une zone console pour afficher les résultats

L’interface utilise plusieurs drawables personnalisés afin d’obtenir un rendu moderne, coloré et visuellement agréable.

---

## Classes principales:

### `ui/MainActivity.java`

Classe principale de l’application.

Elle gère :

- L’initialisation des vues
- Les clics sur les boutons
- La sauvegarde et le chargement des préférences
- L’écriture et la lecture des fichiers internes
- Le test du cache
- L’export externe app-specific
- Le nettoyage complet des données
- L’activation du mode nuit
- L’affichage des résultats dans la console

---

### `prefs/ProfilePrefs.java`

Cette classe gère les préférences non sensibles avec `SharedPreferences`.

Elle permet de stocker :

- Le nom utilisateur
- La langue choisie
- Le mode visuel choisi

Les données sont stockées en `MODE_PRIVATE`.

Exemples de données :

```text
alias = Malak
locale = fr
mood = sunrise / midnight
```

---

### `prefs/SecretVaultPrefs.java`

Cette classe gère le stockage sécurisé du token.

Elle utilise :

- `EncryptedSharedPreferences`
- `MasterKey`
- `AES256_GCM`
- `AES256_SIV`

Le token est stocké dans un espace chiffré et n’est jamais affiché en clair dans l’interface ou dans Logcat.

---

### `model/LearnerRecord.java`

Classe modèle représentant un élément sauvegardé dans le fichier JSON.

Elle contient :

- Un identifiant numérique
- Un nom affiché
- Une spécialité ou un parcours

---

### `storage/TextCapsuleStore.java`

Classe responsable de l’écriture et de la lecture de fichiers texte internes.

Elle utilise :

- `openFileOutput`
- `openFileInput`
- `MODE_PRIVATE`
- Encodage UTF-8

Fichier généré :

```text
lab14_private_note.txt
```

---

### `storage/LearnerJsonStore.java`

Classe responsable de la sauvegarde et du chargement des données JSON.

Elle transforme une liste d’objets `LearnerRecord` en tableau JSON, puis la sauvegarde dans le stockage interne.

Fichier généré :

```text
learners_vault_records.json
```

---

### `cachebox/EphemeralCacheStore.java`

Classe responsable du cache temporaire.

Elle permet de :

- Créer un fichier temporaire
- Lire son contenu
- Purger le dossier cache

Fichier généré :

```text
last_interface_snapshot.tmp
```

Le cache est réservé aux données temporaires et régénérables.

---

### `exportbox/ExternalExportBox.java`

Classe responsable de l’export dans le stockage externe app-specific.

Elle écrit un fichier dans un espace externe propre à l’application, sans utiliser le stockage public.

Fichier généré :

```text
lab14_external_export.txt
```

Ce mécanisme ne nécessite pas de permission de stockage externe classique.

---

## Design:

### Drawables utilisés:

- `bg_screen_blend.xml` : fond général en dégradé
- `bg_header_vault.xml` : header principal coloré
- `bg_card_soft.xml` : carte principale arrondie
- `bg_input_soft.xml` : style des champs de saisie
- `bg_btn_primary.xml` : boutons principaux
- `bg_btn_secondary.xml` : boutons secondaires
- `bg_btn_danger.xml` : bouton de nettoyage
- `bg_result_console.xml` : console de résultat sombre

---

## Mode nuit:

L’application intègre un mode nuit basé sur `AppCompatDelegate`.

Lorsque le switch **Mode nuit** est activé :

- Les préférences sauvegardent l’état `midnight`
- Le thème passe en mode sombre
- Les couleurs sont chargées depuis `values-night/colors.xml`

Lorsque le switch est désactivé :

- L’état devient `sunrise`
- L’application revient au mode clair

---

## Vérification des fichiers internes:

Après avoir cliqué sur :

```text
Créer fichiers internes
```

Les fichiers suivants sont créés dans le stockage interne de l’application :

```text
/data/data/com.malak.vaultprefs14/files/
```

ou selon l’émulateur :

```text
/data/user/0/com.malak.vaultprefs14/files/
```

Fichiers attendus :

```text
learners_vault_records.json
lab14_private_note.txt
```

Ces fichiers peuvent être vérifiés dans Android Studio via :

```text
View > Tool Windows > Device Explorer
```

---

## Vérification du cache:

Après avoir cliqué sur :

```text
Tester cache temporaire
```

Un fichier temporaire est créé dans :

```text
/data/data/com.malak.vaultprefs14/cache/
```

Fichier attendu :

```text
last_interface_snapshot.tmp
```

Après le nettoyage complet, ce fichier est supprimé.

---

## Vérification de l’export externe:

Après avoir cliqué sur :

```text
Exporter fichier externe
```

Un fichier est créé dans le stockage externe app-specific :

```text
/storage/emulated/0/Android/data/com.malak.vaultprefs14/files/
```

Fichier attendu :

```text
lab14_external_export.txt
```

Ce stockage reste lié à l’application et ne nécessite pas de permission de stockage public.

---

## Sécurité appliquée:

L’application respecte plusieurs bonnes pratiques de sécurité :

- Les préférences non sensibles sont stockées séparément des secrets
- Le token est stocké uniquement dans `EncryptedSharedPreferences`
- Le token n’est jamais affiché en clair
- Le token n’est jamais écrit dans Logcat
- Seule la longueur du token est affichée
- Les fichiers internes utilisent `MODE_PRIVATE`
- Les fichiers texte utilisent l’encodage UTF-8
- Les erreurs sont gérées sans exposer de secret
- Le cache est réservé aux données temporaires
- Un bouton de nettoyage complet est disponible
- L’export externe ne contient aucune donnée sensible
- Aucune permission de stockage inutile n’est ajoutée dans le Manifest

---

## Checklist de sécurité:

```text
✅ Aucun token affiché en clair
✅ Aucun token loggé dans Logcat
✅ EncryptedSharedPreferences utilisé pour les secrets
✅ MasterKey utilisé pour protéger le coffre chiffré
✅ SharedPreferences utilisées seulement pour les données non sensibles
✅ MODE_PRIVATE utilisé pour les fichiers internes
✅ Encodage UTF-8 appliqué aux fichiers texte
✅ JSON sauvegardé et chargé avec gestion des erreurs
✅ Cache réservé aux données temporaires
✅ Purge du cache disponible
✅ Export externe limité au stockage app-specific
✅ Nettoyage complet des données disponible
✅ Aucune permission inutile dans AndroidManifest.xml
```

---

## Tests réalisés:

| Test | Résultat attendu |
|---|---|
| Sauvegarder les préférences | Le nom, la langue et le thème sont stockés |
| Charger les préférences | Les valeurs sont restaurées après redémarrage |
| Saisir un token | Le token est stocké chiffré |
| Lire le token | Seule sa longueur est affichée |
| Créer fichiers internes | Les fichiers texte et JSON apparaissent dans `files/` |
| Lire fichiers internes | Les données JSON et la note sont affichées |
| Tester cache | Un fichier temporaire est créé dans `cache/` |
| Exporter fichier externe | Un fichier est créé dans le stockage externe app-specific |
| Nettoyer toutes les données | Les préférences, fichiers, cache et export sont supprimés |

---

## Conclusion:

Ce laboratoire permet de comprendre et d’implémenter plusieurs mécanismes de persistance locale sous Android.

L’application **VaultPrefs14** montre comment stocker correctement différents types de données :

- Les préférences simples avec `SharedPreferences`
- Les secrets avec `EncryptedSharedPreferences`
- Les fichiers texte et JSON en stockage interne
- Les données temporaires dans le cache
- Les exports contrôlés dans le stockage externe app-specific

Ce lab met également en avant des pratiques importantes de sécurité mobile, notamment la séparation des données sensibles, le chiffrement, le contrôle des logs et le nettoyage explicite des données locales.
