package saschpe.gameon.data.core.model

data class FavoritePriceAlerts(
    /**
     * Favorite *plain* to the lowest price map.
     */
    val alerts: Map<String, GameOverview.Price> = mapOf(),
) {
    data class PriceAlert(val favorite: Favorite, val price: GameOverview.Price)
}
