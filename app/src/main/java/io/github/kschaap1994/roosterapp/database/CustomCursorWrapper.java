package io.github.kschaap1994.roosterapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

/**
 * Created by Kevin on 20-10-2016.
 */

public class CustomCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CustomCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public String getSettingName() {
        return getString(getColumnIndex(DbSchema.SettingsTable.Cols.NAME));
    }

    public String getSettingValue() {
        return getString(getColumnIndex(DbSchema.SettingsTable.Cols.VALUE));
    }

}
