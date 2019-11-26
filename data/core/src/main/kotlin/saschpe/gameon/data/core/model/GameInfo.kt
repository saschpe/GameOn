package saschpe.gameon.data.core.model

import kotlinx.serialization.Serializable

@Serializable
data class GameInfo(
    val title: String,
    val image: String?,
    val is_package: Boolean,
    val is_dlc: Boolean,
    val achievements: Boolean,
    val trading_cards: Boolean,
    val early_access: Boolean,
    val reviews: HashMap<String, Review>?,
    val urls: Urls
) {
    @Serializable
    data class Review(
        val perc_positive: Int,
        val total: Int,
        val text: String,
        val timestamp: Long
    ) {
        companion object {
            const val NEUTRAL_REVIEW_THRESHOLD = 60
            const val POSITIVE_REVIEW_THRESHOLD = 80
        }
    }

    @Serializable
    data class Urls(
        val game: String,
        val history: String
    )
}