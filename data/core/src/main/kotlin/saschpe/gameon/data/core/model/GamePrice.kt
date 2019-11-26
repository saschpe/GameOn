package saschpe.gameon.data.core.model

import kotlinx.serialization.Serializable

@Serializable
data class GamePrice(
    val list: List<Price>,
    val urls: Urls
) {
    @Serializable
    data class Price(
        val price_new: Float,
        val price_old: Float,
        val price_cut: Float,
        val url: String,
        val shop: Shop,
        val drm: Set<String>
    ) {
        @Serializable
        data class Shop(
            val id: String,
            val name: String
        )
    }

    @Serializable
    data class Urls(val game: String)

    companion object {
        const val GOOD_PRICE_CUT_THRESHOLD = 0.5f
    }
}