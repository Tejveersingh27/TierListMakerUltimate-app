package app.TierListMakerUltimate.persistence.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import app.TierListMakerUltimate.models.Tier;
import app.TierListMakerUltimate.persistence.TierPersistence;

public class TierPersistenceSQLite implements TierPersistence {
    private final AppDBHelper dbHelper;

    final static String TABLE = AppDBHelper.Tiers.TABLE;
    final static String ID = AppDBHelper.Tiers.COL_ID;
    final static String TIER_LIST_ID = AppDBHelper.Tiers.COL_TIER_LIST_ID;
    final static String NAME = AppDBHelper.Tiers.COL_NAME;
    final static String COLOR_HEX = AppDBHelper.Tiers.COL_COLOR_HEX;
    final static String IS_UNRANKED = AppDBHelper.Tiers.COL_IS_UNRANKED;

    public TierPersistenceSQLite(AppDBHelper helper) {
        this.dbHelper = helper;
    }

    @Override
    public List<Tier> getTiersForList(int tierListId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sel = TIER_LIST_ID + "=?";
        String[] args = new String[]{String.valueOf(tierListId)};

        Cursor c = db.query(
                TABLE,
                new String[]{ID, TIER_LIST_ID, NAME, COLOR_HEX, IS_UNRANKED},
                sel, args, null, null,
                ID + " ASC");

        List<Tier> result = new ArrayList<>();
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
    public Tier getTier(int tierId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sel = ID + "=?";
        String[] args = new String[]{String.valueOf(tierId)};
        Cursor c = db.query(
                TABLE,
                new String[]{ID, TIER_LIST_ID, NAME, COLOR_HEX, IS_UNRANKED},
                sel, args, null, null, null
        );

        Tier result = null;
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
    public Tier insertTier(int tierListId, Tier currentTier) { // Need to remove tierListId parameter???
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TIER_LIST_ID, tierListId);
        cv.put(NAME, currentTier.getName());
        cv.put(COLOR_HEX, currentTier.getColor());
        cv.put(IS_UNRANKED, currentTier.isUnranked() ? 1 : 0);
        long id = db.insertOrThrow(TABLE, null, cv);

        return new Tier(
                (int) id,
                tierListId,
                currentTier.getName(),
                currentTier.getColor(),
                currentTier.isUnranked()
        );
    }

    @Override
    public Tier updateTier(Tier currentTier) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TIER_LIST_ID, currentTier.getTierListId());
        cv.put(NAME, currentTier.getName());
        cv.put(COLOR_HEX, currentTier.getColor());
        cv.put(IS_UNRANKED, currentTier.isUnranked() ? 1 : 0);

        String where = ID + "=?";
        String[] args = new String[]{String.valueOf(currentTier.getId())};
        db.update(TABLE, cv, where, args);

        return currentTier;
    }

    @Override
    public void deleteTier(int tierId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String where = ID + "=?";
        String[] args = new String[]{String.valueOf(tierId)};
        db.delete(AppDBHelper.Tiers.TABLE, where, args); // CASCADE removes the rest
    }

    private static Tier dataFromCursor(Cursor c) {
        int id = c.getInt(c.getColumnIndexOrThrow(ID));
        int listId = c.getInt(c.getColumnIndexOrThrow(TIER_LIST_ID));
        String name = c.getString(c.getColumnIndexOrThrow(NAME));
        String color = c.getString(c.getColumnIndexOrThrow(COLOR_HEX));
        boolean unranked = c.getInt(c.getColumnIndexOrThrow(IS_UNRANKED)) == 1;

        return new Tier(id, listId, name, color, unranked);
    }
}