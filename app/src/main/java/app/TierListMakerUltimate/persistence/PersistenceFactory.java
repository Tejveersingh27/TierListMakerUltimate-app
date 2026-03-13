// Factory class for switching persistence implementations

package app.TierListMakerUltimate.persistence;

import android.content.Context;

import app.TierListMakerUltimate.persistence.sqlite.AppDBHelper;
import app.TierListMakerUltimate.persistence.sqlite.TierListPersistenceSQLite;
import app.TierListMakerUltimate.persistence.sqlite.TierPersistenceSQLite;
import app.TierListMakerUltimate.persistence.sqlite.TierItemPersistenceSQLite;

import app.TierListMakerUltimate.persistence.stubs.TierListPersistenceStub;
import app.TierListMakerUltimate.persistence.stubs.TierPersistenceStub;
import app.TierListMakerUltimate.persistence.stubs.TierItemPersistenceStub;

public final class PersistenceFactory {
    public enum Implementations {
        SQLITE,
        STUB
    }

    public static class Set {
        public final TierListPersistence tierLists;
        public final TierPersistence tiers;
        public final TierItemPersistence items;
    
        private Set(TierListPersistence tl, TierPersistence t, TierItemPersistence i) {
            this.tierLists = tl; this.tiers = t; this.items = i;
        }
    }

    public static Set SQLite(Context context) {
        AppDBHelper dbHelper = new AppDBHelper(context);

        return new Set(
            new TierListPersistenceSQLite(dbHelper),
            new TierPersistenceSQLite(dbHelper),
            new TierItemPersistenceSQLite(dbHelper)
        );
    }

    public static Set Stubs() {
        return new Set(
            new TierListPersistenceStub(),
            new TierPersistenceStub(),
            new TierItemPersistenceStub()
        );
    }
}