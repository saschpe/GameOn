package saschpe.gameon.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class WatchEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "plain") var plain: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "price_threshold") val priceThreshold: Long
)