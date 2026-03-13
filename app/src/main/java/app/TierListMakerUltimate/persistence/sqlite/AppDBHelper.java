package app.TierListMakerUltimate.persistence.sqlite;

import static app.TierListMakerUltimate.persistence.Constants.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import app.TierListMakerUltimate.business.constants.DefaultTiers;
import app.TierListMakerUltimate.persistence.system_data.SeedTemplates;


public class AppDBHelper extends SQLiteOpenHelper {
    private boolean seedDB;

    public AppDBHelper(Context context, String dbName, boolean seedDB) {
        super(context, dbName, null, DB_VERSION);
        this.seedDB = seedDB;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TierLists.CREATE_TABLE);

        db.execSQL(Tiers.CREATE_TABLE);
        db.execSQL(Tiers.CREATE_IDX_TIER_LIST_ID);

        db.execSQL(TierItems.CREATE_TABLE);
        db.execSQL(TierItems.CREATE_IDX_TIER_ID);

        // Load seed data for grader
        if (seedDB) {
            seedData(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP INDEX IF EXISTS " + TierItems.IDX);
        db.execSQL("DROP TABLE IF EXISTS " + TierItems.TABLE);

        db.execSQL("DROP INDEX IF EXISTS " + Tiers.IDX);
        db.execSQL("DROP TABLE IF EXISTS " + Tiers.TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TierLists.TABLE);

        onCreate(db);
    }

    static final class TierLists {
        static final String TABLE = "tier_lists";

        static final String COL_ID = "_id";
        static final String COL_NAME = "name";
        static final String COL_THUMBNAIL = "thumbnail_path";
        static final String COL_IS_TEMPLATE = "is_template";

        static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE + " ("
                        + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COL_NAME + " TEXT NOT NULL, "
                        + COL_THUMBNAIL + " TEXT, "
                        + COL_IS_TEMPLATE + " INTEGER NOT NULL DEFAULT 0"
                        + ");";
    }

    static final class Tiers {
        static final String TABLE = "tiers";
        static final String IDX = "idx_tiers_tier_list_id";

        static final String COL_ID = "_id";
        static final String COL_TIER_LIST_ID = "tier_list_id";
        static final String COL_NAME = "name";
        static final String COL_COLOR_HEX = "color_hex";
        static final String COL_IS_UNRANKED = "is_unranked";
        static final String COL_TIER_POSITION = "tier_position";

        static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE + " ("
                        + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COL_TIER_LIST_ID + " INTEGER NOT NULL, "
                        + COL_NAME + " TEXT NOT NULL, "
                        + COL_COLOR_HEX + " TEXT, "
                        + COL_IS_UNRANKED + " INTEGER NOT NULL DEFAULT 0, "
                        + COL_TIER_POSITION + " INTEGER NOT NULL DEFAULT 0, "
                        + "FOREIGN KEY(" + COL_TIER_LIST_ID + ") REFERENCES "
                        + TierLists.TABLE + "(" + TierLists.COL_ID + ") "
                        + "ON DELETE CASCADE ON UPDATE CASCADE"
                        + ");";

        static final String CREATE_IDX_TIER_LIST_ID =
                "CREATE INDEX IF NOT EXISTS " + IDX + " ON " + TABLE + "(" + COL_TIER_LIST_ID + ");";
    }

    static final class TierItems {
        static final String TABLE = "tier_items";
        static final String IDX = "idx_items_tier_id";


        static final String COL_ID = "_id";
        static final String COL_TIER_ID = "tier_id";
        static final String COL_IMAGE_PATH = "image_path";
        static final String COL_NAME = "name";
        static final String COL_DESCRIPTION = "description";
        static final String COL_EXPLANATION = "explanation";

        static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TIER_ID + " INTEGER NOT NULL, "
                + COL_IMAGE_PATH + " TEXT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_DESCRIPTION + " TEXT, "
                + COL_EXPLANATION + " TEXT, "
                + "FOREIGN KEY(" + COL_TIER_ID + ") REFERENCES "
                + Tiers.TABLE + "(" + Tiers.COL_ID + ") "
                + "ON DELETE CASCADE ON UPDATE CASCADE"
                + ");";

        static final String CREATE_IDX_TIER_ID =
                "CREATE INDEX IF NOT EXISTS " + IDX + " ON " + TABLE + "(" + COL_TIER_ID + ");";
    }


    // Load seed data for grader
    private void seedData(SQLiteDatabase db) {
        for (SeedTemplates.SystemTemplate template : SeedTemplates.SYSTEM_TEMPLATES) {
            // insert tier list
            ContentValues cv = new ContentValues();
            cv.put(TierLists.COL_NAME, template.name());
            cv.put(TierLists.COL_THUMBNAIL, template.thumbnailPath());
            cv.put(TierLists.COL_IS_TEMPLATE, 1);
            long tierListId = db.insertOrThrow(TierLists.TABLE, null, cv);

            // insert default tiers
            for (DefaultTiers tier : DefaultTiers.values()) {
                ContentValues tierCv = new ContentValues();
                tierCv.put(Tiers.COL_TIER_LIST_ID, tierListId);
                tierCv.put(Tiers.COL_NAME, tier.label);
                tierCv.put(Tiers.COL_COLOR_HEX, tier.color);
                tierCv.put(Tiers.COL_IS_UNRANKED, tier.isUnranked ? 1 : 0);
                long tierId = db.insertOrThrow(Tiers.TABLE, null, tierCv);

                // insert items into unranked tier
                if (tier.isUnranked) {
                    for (SeedTemplates.SystemTemplateItem item : template.items()) {
                        ContentValues itemCv = new ContentValues();
                        itemCv.put(TierItems.COL_TIER_ID, tierId);
                        itemCv.put(TierItems.COL_NAME, item.name());
                        itemCv.put(TierItems.COL_IMAGE_PATH, item.imagePath());
                        itemCv.put(TierItems.COL_DESCRIPTION, item.description());
                        db.insertOrThrow(TierItems.TABLE, null, itemCv);
                    }
                }
            }
        }
    }
}