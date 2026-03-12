package app.TierListMakerUltimate.persistence.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

final class AppDBHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "TierListMakerUltimate.db";
    static final int DB_VERSION = 1;

    public AppDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(TierLists.CREATE_TABLE);

        db.execSQL(Tiers.CREATE_TABLE);
        db.execSQL(Tiers.CREATE_IDX_TIER_LIST_ID);

        db.execSQL(TierItems.CREATE_TABLE);
        db.execSQL(TierItems.CREATE_IDX_TIER_ID);
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

        static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TIER_LIST_ID + " INTEGER NOT NULL, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_COLOR_HEX + " TEXT, "
                + COL_IS_UNRANKED + " INTEGER NOT NULL DEFAULT 0, "
                + "FOREIGN KEY(" + COL_TIER_LIST_ID + ") REFERENCES "
                + TierLists.TABLE + "(" + TierLists.COL_ID + ") "
                + "ON DELETE CASCADE ON UPDATE CASCADE"
                + ");";

        static final String CREATE_IDX_TIER_LIST_ID =
                "CREATE INDEX IF NOT EXISTS " + IDX +" ON " + TABLE + "(" + COL_TIER_LIST_ID + ");";
    }

    static final class TierItems {
        static final String TABLE = "tier_items";
        static final String IDX = "idx_items_tier_id";


        static final String COL_ID = "_id";
        static final String COL_TIER_ID = "tier_id";
        static final String COL_IMAGE_PATH = "image_path";
        static final String COL_NAME = "name";
        static final String COL_DESCRIPTION = "description";

        static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TIER_ID + " INTEGER NOT NULL, "
                + COL_IMAGE_PATH + " TEXT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_DESCRIPTION + " TEXT, "
                + "FOREIGN KEY(" + COL_TIER_ID + ") REFERENCES "
                + Tiers.TABLE + "(" + Tiers.COL_ID + ") "
                + "ON DELETE CASCADE ON UPDATE CASCADE"
                + ");";

        static final String CREATE_IDX_TIER_ID =
                "CREATE INDEX IF NOT EXISTS " + IDX + " ON " + TABLE + "(" + COL_TIER_ID + ");";
    }
}