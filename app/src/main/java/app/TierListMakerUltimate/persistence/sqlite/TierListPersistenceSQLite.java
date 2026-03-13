package app.TierListMakerUltimate.persistence.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.persistence.interfaces.TierListPersistence;

public class TierListPersistenceSQLite implements TierListPersistence {
    private final AppDBHelper dbHelper;

    final static String TABLE = AppDBHelper.TierLists.TABLE;
    final static String ID = AppDBHelper.TierLists.COL_ID;
    final static String NAME = AppDBHelper.TierLists.COL_NAME;
    final static String THUMBNAIL = AppDBHelper.TierLists.COL_THUMBNAIL;
    final static String IS_TEMPLATE = AppDBHelper.TierLists.COL_IS_TEMPLATE;


    public TierListPersistenceSQLite(AppDBHelper helper) {
        this.dbHelper = helper;
    }

    @Override
    public List<TierList> getTierLists() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(
                TABLE,
                new String[]{ID, NAME, THUMBNAIL, IS_TEMPLATE},
                null, null, null, null,
                ID + " ASC"
        );

        List<TierList> result = new ArrayList<>();
        try {
            while (c.moveToNext()) {
                result.add(dataFromCursor(c));
            }
        } finally {
            c.close();
        }

        return result;
    }

    @Override
    public List<TierList> getTemplates() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sel = IS_TEMPLATE + "=?";
        String[] args = new String[]{"1"};
        Cursor c = db.query(
                TABLE,
                new String[]{ID, NAME, THUMBNAIL, IS_TEMPLATE},
                sel, args, null, null,
                ID + " ASC"
        );

        List<TierList> result = new ArrayList<>();
        try {
            while (c.moveToNext()) {
                result.add(dataFromCursor(c));
            }
        } finally {
            c.close();
        }

        return result;
    }

    @Override
    public TierList getTierList(int tierListId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sel = ID + "=?";
        String[] args = new String[]{String.valueOf(tierListId)};
        Cursor c = db.query(
                TABLE,
                new String[]{ID, NAME, THUMBNAIL, IS_TEMPLATE},
                sel, args, null, null, null
        );

        TierList result = null;
        try {
            if (c.moveToFirst()) {
                result = dataFromCursor(c);
            }
        } finally {
            c.close();
        }

        return result;
    }

    @Override
    public TierList insertTierList(TierList currTierList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(NAME, currTierList.getName());
        cv.put(THUMBNAIL, currTierList.getThumbnailPath());
        cv.put(IS_TEMPLATE, currTierList.isTemplate() ? 1 : 0);
        long id = db.insertOrThrow(TABLE, null, cv);

        return new TierList(
                (int) id,
                currTierList.getName(),
                currTierList.getThumbnailPath(),
                currTierList.isTemplate()
        );
    }

    @Override
    public TierList updateTierList(TierList currTierList) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(NAME, currTierList.getName());
        cv.put(THUMBNAIL, currTierList.getThumbnailPath());
        cv.put(IS_TEMPLATE, currTierList.isTemplate() ? 1 : 0);

        String where = ID + "=?";
        String[] args = new String[]{String.valueOf(currTierList.getId())};
        db.update(TABLE, cv, where, args);

        return currTierList;
    }

    @Override
    public void deleteTierList(int tierListId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String where = ID + "=?";
        String[] args = new String[]{String.valueOf(tierListId)};
        db.delete(TABLE, where, args); // CASCADE removes the rest
    }

    @Override
    public List<TierList> getNonTemplateTierLists() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sel = IS_TEMPLATE + "=?";
        String[] args = new String[]{"0"};
        Cursor c = db.query(
                TABLE,
                null, sel, args, null, null,
                ID + " ASC"
        );

        List<TierList> result = new ArrayList<>();
        try {
            while (c.moveToNext()) {
                result.add(dataFromCursor(c));
            }
        } finally {
            c.close();
        }

        return result;
    }

    // Helper method, returns a TierList with data from a Cursor
    private static TierList dataFromCursor(Cursor c) {
        int id = c.getInt(c.getColumnIndexOrThrow(ID));
        String name = c.getString(c.getColumnIndexOrThrow(NAME));
        String thumb = c.getString(c.getColumnIndexOrThrow(THUMBNAIL));
        boolean isTemplate = c.getInt(c.getColumnIndexOrThrow(IS_TEMPLATE)) == 1;

        return new TierList(id, name, thumb, isTemplate);
    }
}
