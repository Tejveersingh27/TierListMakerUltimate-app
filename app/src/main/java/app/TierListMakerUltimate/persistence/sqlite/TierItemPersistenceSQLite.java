package app.TierListMakerUltimate.persistence.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import app.TierListMakerUltimate.models.TierItem;
import app.TierListMakerUltimate.persistence.TierItemPersistence;

public class TierItemPersistenceSQLite implements TierItemPersistence {

    private final AppDBHelper dbHelper;

    final static String TABLE = AppDBHelper.TierItems.TABLE;
    final static String ID = AppDBHelper.TierItems.COL_ID;
    final static String TIER_ID = AppDBHelper.TierItems.COL_TIER_ID;
    final static String IMAGE_PATH = AppDBHelper.TierItems.COL_IMAGE_PATH;
    final static String NAME = AppDBHelper.TierItems.COL_NAME;
    final static String DESCRIPTION = AppDBHelper.TierItems.COL_DESCRIPTION;

    public TierItemPersistenceSQLite(AppDBHelper helper) {
        this.dbHelper = helper;
    }

    @Override
    public List<TierItem> getItemsForTier(int tierId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sel = TIER_ID + "=?";
        String[] args = new String[]{String.valueOf(tierId)};
        Cursor c = db.query(
                TABLE,
                new String[]{ID, TIER_ID, IMAGE_PATH, NAME, DESCRIPTION},
                sel, args, null, null,
                ID + " ASC");

        List<TierItem> result = new ArrayList<>();
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
    public TierItem getItem(int itemId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sel = ID + "=?";
        String[] args = new String[]{String.valueOf(itemId)};

        Cursor c = db.query(
                TABLE,
                new String[]{ID, TIER_ID, IMAGE_PATH, NAME, DESCRIPTION},
                sel, args, null, null, null
        );

        TierItem result = null;
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
    public TierItem insertItem(int tierId, TierItem currentItem) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TIER_ID, tierId);
        cv.put(IMAGE_PATH, currentItem.getImagePath());
        cv.put(NAME, currentItem.getName());
        cv.put(DESCRIPTION, currentItem.getDescription());
        long id = db.insertOrThrow(TABLE, null, cv);

        return new TierItem(
                (int) id,
                currentItem.getImagePath(),
                currentItem.getName(),
                currentItem.getDescription(),
                tierId
        );
    }

    @Override
    public TierItem updateItem(TierItem currentItem) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TIER_ID, currentItem.getTierId());
        cv.put(IMAGE_PATH, currentItem.getImagePath());
        cv.put(NAME, currentItem.getName());
        cv.put(DESCRIPTION, currentItem.getDescription());

        String where = ID + "=?";
        String[] args = new String[]{String.valueOf(currentItem.getId())};
        db.update(TABLE, cv, where, args);

        return currentItem;
    }

    @Override
    public void deleteItem(int itemId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String where = ID + "=?";
        String[] args = new String[]{String.valueOf(itemId)};
        db.delete(TABLE, where, args);
    }

    private static TierItem dataFromCursor(Cursor c) {
        int id = c.getInt(c.getColumnIndexOrThrow(ID));
        int tierId = c.getInt(c.getColumnIndexOrThrow(TIER_ID));
        String image = c.getString(c.getColumnIndexOrThrow(IMAGE_PATH));
        String name = c.getString(c.getColumnIndexOrThrow(NAME));
        String desc = c.getString(c.getColumnIndexOrThrow(DESCRIPTION));
        return new TierItem(id, image, name, desc, tierId);
    }
}