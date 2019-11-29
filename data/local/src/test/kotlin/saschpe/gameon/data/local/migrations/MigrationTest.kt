package saschpe.gameon.data.local.migrations

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import saschpe.gameon.data.local.AppDatabase

@RunWith(AndroidJUnit4::class)
class MigrationTest {
    @get:Rule
    val migrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java.canonicalName
    )

    @Test
    fun migrate1To2() {
        migrationTestHelper.createDatabase(TEST_DB, 1).apply {
            // db has schema version 1. insert some data using SQL queries.
            // You cannot use DAO classes because they expect the latest schema.
            execSQL(
                """
                INSERT INTO favorites (created_at, plain, title, price_threshold)
                VALUES (123, 'foo', 'Foo', 1);
                """.trimIndent()
            )
            // Prepare for the next version.
            close()
        }

        // Re-open the database with version 2 and provide Migration1to2 as the migration process.
        migrationTestHelper.runMigrationsAndValidate(TEST_DB, 2, true, Migration1to2)

        // MigrationTestHelper automatically verifies the schema changes,
        // but you need to validate that the data was migrated properly.
    }

    @Test
    fun migrateAll() {
        // Create earliest version of the database.
        migrationTestHelper.createDatabase(TEST_DB, 1).apply {
            close()
        }

        // Open latest version of the database. Room will validate the schema
        // once all migrations execute.
        Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AppDatabase::class.java,
            TEST_DB
        ).addMigrations(*ALL_MIGRATIONS).build().apply {
            openHelper.writableDatabase
            close()
        }
    }

    companion object {
        private const val TEST_DB = "migration-test.db"
        private val ALL_MIGRATIONS = arrayOf(Migration1to2)
    }
}