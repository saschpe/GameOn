package saschpe.gameon.data.core.model

data class Favorite(
    val id: Int? = null,
    val plain: String,
    val createdAt: Long = System.currentTimeMillis(),
    val priceThreshold: Double? = null
)