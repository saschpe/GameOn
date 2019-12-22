package saschpe.gameon.data.core.model

data class FavoritePriceAlerts(
    /**
     * Favorite *plain* to lowest price map.
     */
    val alerts: Map<String, GameOverview.Lowest> = mapOf()
) {
    data class PriceAlert(
        val favorite: Favorite,
        val lowest: GameOverview.Lowest
    )
}