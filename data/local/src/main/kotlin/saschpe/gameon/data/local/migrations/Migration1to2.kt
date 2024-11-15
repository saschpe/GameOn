package saschpe.gameon.data.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Removes 'title' column from the 'favorites' table.
 *
 * Check <a href="https://www.sqlite.org/lang_altertable.html#otheralter">SQLite documentation</a>.
 *
 * @see saschpe.gameon.data.local.model.FavoriteEntity
 */
internal val Migration1to2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
CREATE TABLE IF NOT EXISTS new_favorites
(
id              INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
created_at      INTEGER                           NOT NULL,
plain           TEXT                              NOT NULL,
price_threshold INTEGER
);"""
        )
        db.execSQL(
            """
INSERT INTO new_favorites (id, created_at, plain, price_threshold)
SELECT id, created_at, plain, price_threshold
FROM favorites;
"""
        )
        db.execSQL("DROP TABLE favorites;")
        db.execSQL("ALTER TABLE new_favorites RENAME TO favorites;")
    }
}
