package io.github.kschaap1994.roosterapp.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 20-10-2016.
 */

public class DbLab {

    private static DbLab lab;
    private final Map<String, String> settings = new HashMap<>();
    private Context context;
    private SQLiteDatabase db;

    private DbLab(final Context context) {
        this.context = context.getApplicationContext();
        db = new DbHelper(context).getWritableDatabase();
    }

    public static DbLab get(final Context context) {
        if (lab == null) {
            lab = new DbLab(context);
        }
        return lab;
    }

    private static ContentValues getContentValues(final String name, final String value) {
        final ContentValues values = new ContentValues();
        values.put(DbSchema.SettingsTable.Cols.NAME, name);
        values.put(DbSchema.SettingsTable.Cols.VALUE, value);

        return values;
    }

    private CustomCursorWrapper query(String whereClause, String[] whereArgs) {
        @SuppressLint("Recycle") Cursor cursor = db.query(
                DbSchema.SettingsTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        return new CustomCursorWrapper(cursor);
    }

    public void addOrUpdateSetting(final String name, final String value) {
        final ContentValues values = getContentValues(name, value);

        if (settings.containsKey(name))
            db.update(DbSchema.SettingsTable.NAME, values, null, null);
        else
            db.insert(DbSchema.SettingsTable.NAME, null, values);
    }

    public boolean hasSettings() {
        try (CustomCursorWrapper cursor = query(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                settings.put(cursor.getSettingName(), cursor.getSettingValue());
                cursor.moveToNext();
            }
        }
        return !settings.isEmpty();
    }

    public String getSetting(final String name) {
        return settings.get(name);
    }
}
