package saschpe.gameon.data.core.model

data class Watch(
    val plain: String,
    val title: String,
    val createdAt: Long = System.currentTimeMillis(),
    val priceThreshold: Long
)