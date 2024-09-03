package saschpe.gameon.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Adds 'dismissed' column to 'favorites' table.
 *
 * Check <a href="https://www.sqlite.org/lang_altertable.html#otheralter">SQLite documentation</a>.
 *
 * @see saschpe.gameon.data.local.model.FavoriteEntity
 */
internal val Migration2to3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
ALTER TABLE favorites
    ADD COLUMN dismissed INTEGER NOT NULL DEFAULT 0
        CHECK (dismissed IN (0, 1));
            """
        )
    }
}
