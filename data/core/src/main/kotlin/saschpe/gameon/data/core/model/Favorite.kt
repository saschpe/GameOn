package saschpe.gameon.data.core.model

data class Favorite(
    val plain: String,
    val createdAt: Long = System.currentTimeMillis(),
    val priceThreshold: Long?
)